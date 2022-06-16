package com.example.magmarket.ui.placedordersfragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.magmarket.R
import com.example.magmarket.data.remote.Resource
import com.example.magmarket.databinding.FragmentEachPlacedOrderBinding
import com.example.magmarket.databinding.FragmentMyOrdersBinding
import com.example.magmarket.ui.adapters.PlacedOrderDetailsAdapter
import com.example.magmarket.ui.adapters.PlacedOrderDetailsAdapterDiffCall
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EachPlacedOrderDetailsFragment : Fragment(R.layout.fragment_each_placed_order) {
    private var _binding: FragmentEachPlacedOrderBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<EachPlacedOrderDetailsFragmentArgs>()
    private val placedAdapter = PlacedOrderDetailsAdapter()
    private val cartViewModel by activityViewModels<MyOrdersViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEachPlacedOrderBinding.bind(view)
        init()
        getResponseOrder()
        close()
    }

    private fun init() {
        cartViewModel.getAnOrder(args.placedOrderId)
        binding.recyclerviewEachproduct.adapter = placedAdapter
    }

    private fun getResponseOrder() = with(binding) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.orderList.collect {
                    when (it) {
                        is Resource.Loading -> {
                            stateView.onLoading()
                            recyclerviewEachproduct.isVisible = false
                            imgClose.isVisible = false
                        }
                        is Resource.Success -> {
                            stateView.onSuccess()
                            placedAdapter.submitList(it.value.line_items)
                            recyclerviewEachproduct.isVisible = true
                            imgClose.isVisible = true
                        }
                        is Resource.Error -> {
                            stateView.onFail()
                            imgClose.isVisible = true
                        }
                    }

                }
            }
        }
    }

    private fun close() {
        binding.imgClose.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}