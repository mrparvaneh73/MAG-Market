package com.example.magmarket.ui.cartfragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.magmarket.R
import com.example.magmarket.data.remote.model.ProductRecyclerViewItem
import com.example.magmarket.databinding.FragmentMyOrdersBinding
import com.example.magmarket.ui.adapters.OrderPlacedAdapter
import com.example.magmarket.utils.ResultWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyOrdersFragment : Fragment(R.layout.fragment_my_orders) {
    private var _binding: FragmentMyOrdersBinding? = null
    private val binding get() = _binding!!
    private val placedOrderId= mutableListOf<Int>(0)
    private val cartViewModel by activityViewModels<CartViewModel>()
    val orderPlacedAdapter = OrderPlacedAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMyOrdersBinding.bind(view)
        binding.rcOrderplaced.adapter = orderPlacedAdapter
        getOrdersIdFromLocal()
        collect()
    }

    private fun getOrdersIdFromLocal() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.getAllPlacedOrders().collect {
                    for (i in it){
                        placedOrderId.add(i.id)
                    }
                    Log.d("itchiyeh", "getOrdersIdFromLocal: " +placedOrderId.toString())
                    cartViewModel.getPlacedOrder(placedOrderId.toString())
                }
            }
        }
    }

    private fun collect() {
        cartViewModel.order.collectIt(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {
                    binding.stateView.onLoading()
                    binding.rcOrderplaced.isVisible = false
                }
                is ResultWrapper.Success -> {
                    Log.d("responseordersman ", "collect: "+it.value)
                    orderPlacedAdapter.submitList(it.value)
                    binding.rcOrderplaced.isVisible = true
                    if (it.value.isNotEmpty()) {
                        binding.stateView.onSuccess()
                    } else {
                        binding.stateView.onEmpty()
                    }
                }
                is ResultWrapper.Error -> {
                    binding.stateView.onFail()

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
}