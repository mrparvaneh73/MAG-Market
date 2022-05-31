package com.example.magmarket.ui.cartfragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.magmarket.R
import com.example.magmarket.data.local.entities.ProductItemLocal
import com.example.magmarket.data.remote.model.order.LineItem
import com.example.magmarket.data.remote.model.order.Order
import com.example.magmarket.databinding.FragmentCartBinding
import com.example.magmarket.ui.adapters.CartAdapter
import com.example.magmarket.utils.ResultWrapper
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
    private val cartViewModel by viewModels<CartViewModel>()
    val cartAdapter = CartAdapter()
    var count = 1
    var totalPrice: Int = 0
    var listLineItem: MutableList<LineItem> = mutableListOf()
    var allProductInOrderList: MutableList<ProductItemLocal> = mutableListOf()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCartBinding.bind(view)


        ObserveOrderList()
        ResponseOfOrder()
        adapterClickListener()
        calculatePrices()
    }

    private fun ObserveOrderList() {
        binding.cartRecyclerviw.adapter = cartAdapter

        cartViewModel.getOrdersFromLocal().observe(viewLifecycleOwner) {
            allProductInOrderList.addAll(it)
            cartAdapter.submitList(it)
            for (i in it) {
                listLineItem.add(LineItem(product_id = i.id, quantity = i.count, variation_id = 0))
            }
            if (it.isNotEmpty()) {
                for (i in it) {
                    totalPrice = (i.price!!.toInt() * i.count)
                }
                binding.tvTotalprice.text = nf.format(totalPrice.toInt())
                Log.d("totalprice", "calculatePrices: " + totalPrice)

            }
            creatOrder(Order(listLineItem))
            binding.toolbarcount.text = cartAdapter.itemCount.toString()
        }
    }

    private fun calculatePrices() {


    }


    private fun adapterClickListener() {
        cartAdapter.setOnItemClickListener(object : CartAdapter.OnItemClickListener {
            override fun onItemPlus(position: Int) {

                cartViewModel.updateOrder(
                    ProductItemLocal(
                        id = cartAdapter.currentList[position].id,
                        count = cartAdapter.currentList[position].count.plus(1),
                        name = cartAdapter.currentList[position].name,
                        price = cartAdapter.currentList[position].price,
                        images = cartAdapter.currentList[position].images
                    )
                )
                cartAdapter.notifyItemChanged(position)
                for (i in cartAdapter.currentList){
                    totalPrice = (i.price!!.toInt() * i.count)
                }

            }

            override fun onItemMinus(position: Int) {

                if (cartAdapter.currentList[position].count == 1) {
                    cartViewModel.deleteOrderFromLocal(cartAdapter.currentList[position])
                    cartAdapter.notifyItemRemoved(position)
                    binding.toolbarcount.text = cartAdapter.itemCount.toString()
                    for (i in cartAdapter.currentList){
                        totalPrice = (i.price!!.toInt() * i.count)
                    }

                } else {
                    cartViewModel.updateOrder(
                        ProductItemLocal(
                            id = cartAdapter.currentList[position].id,
                            count = cartAdapter.currentList[position].count.minus(1),
                            name = cartAdapter.currentList[position].name,
                            price = cartAdapter.currentList[position].price,
                            images = cartAdapter.currentList[position].images
                        )
                    )
                    for (i in cartAdapter.currentList){
                        totalPrice = (i.price!!.toInt() * i.count)
                    }
                    cartAdapter.notifyItemChanged(position)

                }


            }

        })
    }

    private fun creatOrder(order: Order) {
        binding.buttonAddToCart.setOnClickListener {
            cartViewModel.creatOrder(order)
        }
    }

    private fun ResponseOfOrder() {
        cartViewModel.orderList.collectIt(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {

                }
                is ResultWrapper.Success -> {

                    Log.d("includekarmikoneh", "onViewCreated: " + it.value.toString())

                }
                is ResultWrapper.Error -> {


                }
            }
        }
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