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
import com.example.magmarket.databinding.FragmentCartBinding
import com.example.magmarket.ui.adapters.CartAdapter
import com.example.magmarket.utils.ResultWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val cartviewmodel by viewModels<CartViewModel>()
    var ordersId: MutableList<Int> = mutableListOf()
    var productItemLocal: MutableList<ProductItemLocal> = mutableListOf()
    val cartAdapter = CartAdapter()
    var count = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCartBinding.bind(view)
        binding.cartRecyclerviw.adapter = cartAdapter
        cartviewmodel.getOrdersFromLocal().observe(viewLifecycleOwner) {
            productItemLocal.addAll(it)
            cartAdapter.submitList(it)
//            productItemLocal.addAll(it)
//            for (i in it) {
//                ordersId.add(i.id)
//
//            }
//            Log.d("tedad", "onViewCreated: " + ordersId)
//            cartviewmodel.getAllOrders(ordersId.toString())
        }

        Log.d("tedadorderid", "onViewCreated: " + ordersId)


//        cartviewmodel.orderList.collectIt(viewLifecycleOwner) {
//            when (it) {
//                is ResultWrapper.Loading -> {
//
//                }
//                is ResultWrapper.Success -> {
//                    for (s in productItemLocal.size..0) {
//                        if (productItemLocal[s].id == it.value[s].id.toInt()) {
//                            it.value[s].count = productItemLocal[s].count
//                        }
//                    }
//                    Log.d("includekarmikoneh", "onViewCreated: " + it.value.toString())
//                    cartAdapter.submitList(it.value)
//                    binding.toolbarcount.text = ((cartAdapter.itemCount) - 1).toString()
//
//                    if (it.value.isNotEmpty()) {
//
//                    } else {
//
//                    }
//                }
//                is ResultWrapper.Error -> {
//
//
//                }
//            }
//        }
        adapterClickListener()
    }

    private fun adapterClickListener() {
        cartAdapter.setOnItemClickListener(object : CartAdapter.OnItemClickListener {
            override fun onItemPlus(position: Int) {

                cartviewmodel.updateOrder(
                    ProductItemLocal(
                        productItemLocal[position].id,
                        count = cartAdapter.currentList[position].count.plus(1),
                        name = productItemLocal[position].name,
                        price = productItemLocal[position].price,
                        images = productItemLocal[position].images
                    )
                )
                cartAdapter.notifyItemChanged(position)

            }

            override fun onItemMinus(position: Int) {

                if (cartAdapter.currentList[position].count == 1) {
                    cartviewmodel.deleteOrderFromLocal(cartAdapter.currentList[position])
                    cartAdapter.notifyItemRemoved(position)
                }else{
                    cartviewmodel.updateOrder(
                        ProductItemLocal(
                            productItemLocal[position].id,
                            count = cartAdapter.currentList[position].count.minus(1),
                            name = productItemLocal[position].name,
                            price = productItemLocal[position].price,
                            images = productItemLocal[position].images
                        )
                    )
                    cartAdapter.notifyItemChanged(position)

                }




            }

        })
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