package com.example.magmarket.ui.cartfragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.magmarket.R
import com.example.magmarket.data.local.entities.OrderList
import com.example.magmarket.data.local.entities.ProductItemLocal
import com.example.magmarket.data.remote.model.order.LineItem
import com.example.magmarket.data.remote.model.order.Order
import com.example.magmarket.databinding.FragmentCartBinding
import com.example.magmarket.ui.adapters.CartAdapter

import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*
import kotlin.math.absoluteValue

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val nf: NumberFormat = NumberFormat.getInstance(Locale.US)
    private val cartViewModel by activityViewModels<CartViewModel>()
    val cartAdapter = CartAdapter()
    var count = 1
    var totalPrice = 0
    var totalOff = 0
    var totalCount = 0
    var totalPriceWithoutOff = 0
    var listLineItem: MutableList<LineItem> = mutableListOf()
    var allProductInOrderList: MutableList<ProductItemLocal> = mutableListOf()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCartBinding.bind(view)
        binding.cartRecyclerviw.adapter = cartAdapter

        ObserveOrderList()

        adapterClickListener()

        finalizeOrder()

    }

    private fun ObserveOrderList() {

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.getOrdersFromLocal().collect {
                    allProductInOrderList.addAll(it)
                    cartAdapter.submitList(it)
                    for (i in it) {
                        listLineItem.add(
                            LineItem(
                                product_id = i.id,
                                quantity = i.count,
                                variation_id = 0
                            )
                        )
                    }
                    if (cartViewModel.isSuccess==true) {
                        for (i in it) {
                            cartViewModel.deleteOrderFromLocal(i)
                        }
                    cartViewModel.isSuccess=false
                    }
                    if (it.isNotEmpty()) {
                        binding.parent.isVisible = true
                        binding.detailCard.isVisible = true
                        binding.emptycart.isVisible = false

                        for (i in it) {
                            totalPrice += (i.price!!.toInt() * i.count)
                            totalOff += (i.off)
                            totalPriceWithoutOff += (i.regular_price!!.toInt() * i.count)
                            totalCount += i.count

                        }
                        init()
                    } else {
                        binding.parent.isVisible = false
                        binding.detailCard.isVisible = false
                        binding.emptycart.isVisible = true
                    }

                }
            }

        }

    }

    private fun init() {

        binding.tvTotalprice.text = nf.format(totalPrice)
        binding.tvTotaloffCountPrice.text = nf.format(totalOff)
        binding.totlapricedesc.text = nf.format(totalPrice)
        binding.totalPriceWithoutOff.text = nf.format(totalPriceWithoutOff)
        binding.countproductdesc.text = totalCount.toString()
    }

    private fun adapterClickListener() {
        cartAdapter.setOnItemClickListener(object : CartAdapter.OnItemClickListener {
            override fun onItemPlus(position: Int) {
                resetValues()
                cartViewModel.updateOrder(
                    ProductItemLocal(
                        id = cartAdapter.currentList[position].id,
                        count = cartAdapter.currentList[position].count.plus(1),
                        name = cartAdapter.currentList[position].name,
                        price = cartAdapter.currentList[position].price,
                        images = cartAdapter.currentList[position].images,
                        regular_price = cartAdapter.currentList[position].regular_price,
                        sale_price = cartAdapter.currentList[position].sale_price,
                        off = cartAdapter.currentList[position].off.plus(
                            cartAdapter.currentList[position].regular_price!!.toInt()
                                .minus(cartAdapter.currentList[position].price!!.toInt())
                        )
                    )
                )
                cartAdapter.notifyItemChanged(position)


            }

            override fun onItemMinus(position: Int) {
                resetValues()
                if (cartAdapter.currentList[position].count == 1) {
                    cartViewModel.deleteOrderFromLocal(cartAdapter.currentList[position])
                    cartAdapter.notifyItemRemoved(position)
                } else {
                    cartViewModel.updateOrder(
                        ProductItemLocal(
                            id = cartAdapter.currentList[position].id,
                            count = cartAdapter.currentList[position].count.minus(1),
                            name = cartAdapter.currentList[position].name,
                            price = cartAdapter.currentList[position].price,
                            images = cartAdapter.currentList[position].images,
                            regular_price = cartAdapter.currentList[position].regular_price,
                            sale_price = cartAdapter.currentList[position].sale_price,
                            off = cartAdapter.currentList[position].off.minus(
                                cartAdapter.currentList[position].regular_price!!.toInt()
                                    .minus(cartAdapter.currentList[position].price!!.toInt())
                            )
                        )
                    )

                    cartAdapter.notifyItemChanged(position)

                }


            }

        })
    }

    private fun finalizeOrder() {
        binding.buttonAddToCart.setOnClickListener {
            isUserLogin()
        }
    }

    private fun isUserLogin() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.getUserFromLocal().collect {
                    if (it.isNotEmpty()) {
                        findNavController().navigate(
                            CartFragmentDirections.actionCartFragmentToFinalizeOrderFragment(
                                it[it.lastIndex].id
                            )
                        )
                    } else {
                        openDialog()
                    }
                }
            }
        }
    }


    fun resetValues() {
        listLineItem.clear()
        totalPrice = 0
        totalCount = 0
        totalOff = 0
        totalPriceWithoutOff = 0
    }
    private fun openDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.first_login)
        val button = dialog.findViewById<ImageView>(R.id.img_close)
        val button_login = dialog.findViewById<MaterialButton>(R.id.btn_login)

        button_login.setOnClickListener {
           (parentFragment as ParentOfCartFragment).navigate()
            dialog.dismiss()

        }
        button.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    private fun <T> StateFlow<T>.collectIt(lifecycleOwner: LifecycleOwner, function: (T) -> Unit) {
        lifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collect {
                    function.invoke(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}