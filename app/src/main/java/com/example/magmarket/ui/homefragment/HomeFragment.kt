package com.example.magmarket.ui.homefragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.magmarket.R
import com.example.magmarket.adapters.ProductAdapter
import com.example.magmarket.databinding.FragmentHomeBinding
import com.example.magmarket.utils.ResultWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private val bestAdapter = ProductAdapter()
    private val newstAdapter = ProductAdapter()
    private val mostViewsAdapter = ProductAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        binding.newestRecyclerview.adapter = newstAdapter
        binding.mostViewrecyclerview.adapter = mostViewsAdapter
        binding.bestSellerRecyclerview.adapter = bestAdapter

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bestProduct.collect {
                    when (it) {
                        is ResultWrapper.Loading -> binding.stateView.onLoading()
                        is ResultWrapper.Success -> {
                            Log.d("hello", "onViewCreated: " + it.value.toString())
                            bestAdapter.submitList(it.value)
                            if (it.value.isNotEmpty()) {
                                binding.stateView.onSuccess()
                            } else {
                                binding.stateView.onEmpty()
                            }
                        }
                        is ResultWrapper.Error -> {
                            binding.stateView.onFail()
                            binding.stateView.clickRequest {
                                viewModel.getBestProductList()
                            }
                            Log.d("errorr", "onViewCreated: " + it.message)
                            Toast.makeText(
                                requireActivity(),
                                it.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.mostViewProduct.collect {
                    when (it) {
                        is ResultWrapper.Loading -> binding.stateView.onLoading()
                        is ResultWrapper.Success -> {
                            Log.d("hello", "onViewCreated: " + it.value.toString())
                            mostViewsAdapter.submitList(it.value)
                            if (it.value.isNotEmpty()) {
                                binding.stateView.onSuccess()
                            } else {
                                binding.stateView.onEmpty()
                            }
                        }
                        is ResultWrapper.Error -> {
                            binding.stateView.onFail()
                            binding.stateView.clickRequest {
                                viewModel.getMostViewProductList()
                            }
                            Log.d("errorr", "onViewCreated: " + it.message)
                            Toast.makeText(
                                requireActivity(),
                                it.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.newstProduct.collect {
                    when (it) {
                        is ResultWrapper.Loading -> binding.stateView.onLoading()
                        is ResultWrapper.Success -> {
                            Log.d("hello", "onViewCreated: " + it.value.toString())
                            newstAdapter.submitList(it.value)
                            if (it.value.isNotEmpty()) {
                                binding.stateView.onSuccess()
                            } else {
                                binding.stateView.onEmpty()
                            }
                        }
                        is ResultWrapper.Error -> {
                            binding.stateView.onFail()
                            binding.stateView.clickRequest {
                                viewModel.getNewstProductList()
                            }
                            Log.d("errorr", "onViewCreated: " + it.message)
                            Toast.makeText(
                                requireActivity(),
                                it.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}