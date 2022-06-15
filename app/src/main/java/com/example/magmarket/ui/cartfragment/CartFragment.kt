package com.example.magmarket.ui.cartfragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.magmarket.R
import com.example.magmarket.data.remote.Resource
import com.example.magmarket.data.remote.model.order.LineItem
import com.example.magmarket.databinding.FragmentCartBinding
import com.example.magmarket.ui.adapters.CartAdapter

import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val nf: NumberFormat = NumberFormat.getInstance(Locale.US)
    private val cartViewModel by activityViewModels<CartViewModel>()
    private val cartAdapter = CartAdapter()


    var totalPrice = 0
    var totalOff = 0

    var totalCount = 0
    var totalWithoutOff = 0
    var listLineItem: MutableList<LineItem> = mutableListOf()
    var allProductInOrderList: MutableList<Int> = mutableListOf(0)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCartBinding.bind(view)
        binding.cartRecyclerviw.adapter = cartAdapter
        isUserLogin()

        adapterClickListener()

        finalizeOrder()


    }

    private fun isUserLogin() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.getUser().collect {
                    if (it.isLogin){
                        if (it.myorderId != 0) {
                            cartViewModel.orderId = it.myorderId
                            cartViewModel.getAnOrder()
                            collectOnCreate()
                        }
                    }else{
                        openDialog()
                    }

                }
            }
        }
    }

    private fun collectOnCreate() = with(binding) {

        cartViewModel.orderList.collectIt(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    binding.stateView.onSuccess()

                    if (it.value.line_items.isNotEmpty()) {
                        cartViewModel.lineItem.clear()
                        cartViewModel.lineItem.addAll(it.value.line_items)
                        Log.d("productssss", "lineItem: " + it.value.line_items.toString())
                        cartViewModel.productIdFromLineItem()
                        getRemote()
                    } else {
                        emptycart.isVisible = true
                    }
                }
                is Resource.Error -> {
                    binding.stateView.onFail()


                }
            }
        }

    }

    private fun getRemote() = with(binding) {
        cartViewModel.remoteProducts.collectIt(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    stateView.onLoading()
                    parent.isVisible = false
                    buttomBar.isVisible = false
                }
                is Resource.Success -> {
                    binding.parent.isVisible = true
                    binding.detailCard.isVisible = true
                    binding.emptycart.isVisible = false
                    cartViewModel.productItems.clear()
                    cartViewModel.productItems.addAll(it.value)
                    Log.d("productssss", "getRemote: " + it.value.toString())
                    cartAdapter.submitList(cartViewModel.myCart())

                    cartAdapter.notifyDataSetChanged()
                    for (x in cartViewModel.myCart()) {
                        totalPrice += (x.price.toInt() * x.count)
                        totalOff += (x.off)
                        totalWithoutOff += (x.regular_price.toInt() * x.count)
                        totalCount += x.count

                    }
                    init()
                    stateView.onSuccess()
                }
                is Resource.Error -> {

                }
            }
        }
    }

    private fun init() = with(binding) {

        tvTotalprice.text = nf.format(totalPrice)
        tvTotaloffCountPrice.text = nf.format(totalOff)
        totlapricedesc.text = nf.format(totalPrice)
        totalPriceWithoutOff.text = nf.format(totalWithoutOff)
        countproductdesc.text = totalCount.toString()
    }

    private fun adapterClickListener() {
        cartAdapter.setOnItemClickListener(object : CartAdapter.OnItemClickListener {
            override fun onItemPlus(position: Int) {
                resetValues()
                Log.d("clicked", "onItemPlus: plus")
                cartViewModel.plus(position)

                collectWhenUpdate()
            }

            override fun onItemMinus(position: Int) {
                resetValues()

                cartViewModel.minus(position)

                collectWhenUpdate()
            }

        })
    }

    private fun collectWhenUpdate() = with(binding) {
        cartViewModel.orderUpdate.collectIt(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {


                    if (it.value.line_items.isNotEmpty()) {
                        binding.stateView.onSuccess()

                        cartViewModel.lineItem.clear()

                        cartViewModel.lineItem.addAll(it.value.line_items)
                        cartViewModel.productIdFromLineItem()
                        getRemote()
                    } else {

                        binding.emptycart.isVisible = true
                    }
                }
                is Resource.Error -> {
                    binding.stateView.onFail()


                }
            }
        }
    }

    private fun finalizeOrder() {
        binding.buttonAddToCart.setOnClickListener {
//            isUserLogin()
        }
    }


    fun resetValues() {
        listLineItem.clear()
        totalPrice = 0
        totalCount = 0
        totalOff = 0
        totalWithoutOff = 0
    }

    private fun openDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.first_login)
        val button = dialog.findViewById<ImageView>(R.id.img_close)
        val button_login = dialog.findViewById<MaterialButton>(R.id.btn_login)

        button_login.setOnClickListener {
            findNavController().navigate(CartFragmentDirections.actionGlobalUserFragment())
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