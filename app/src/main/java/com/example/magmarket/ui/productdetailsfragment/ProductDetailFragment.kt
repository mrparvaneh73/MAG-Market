package com.example.magmarket.ui.productdetailsfragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.magmarket.R
import com.example.magmarket.adapters.SliderAdapter
import com.example.magmarket.databinding.FragmentProductDetailBinding
import com.example.magmarket.ui.homefragment.HomeViewModel
import com.example.magmarket.utils.ResultWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.log

@AndroidEntryPoint
class ProductDetailFragment : Fragment(R.layout.fragment_product_detail) {
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    val args by navArgs<ProductDetailFragmentArgs>()
    private val viewModel: ProductDetailsViewModel by viewModels()
    val adapter= SliderAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductDetailBinding.bind(view)
        viewModel.getProduct(args.id)
        binding.productSlider.adapter=adapter
        binding.productSlider.clipToPadding=false
        binding.productSlider.clipChildren=false
        binding.productSlider.getChildAt(0).overScrollMode=RecyclerView.OVER_SCROLL_NEVER
        binding.springDotsIndicator.attachTo(binding.productSlider)
        Log.d("iddddd", "onViewCreated: "+args.id)
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.product.collect {
                    when (it) {
                        is ResultWrapper.Loading ->{
                            binding.stateView.onLoading()
                            binding.scrollView3.isVisible=false
                            binding.buttomBar.isVisible=false
                        }
                        is ResultWrapper.Success -> {
                           binding.tvProductName.text=it.value.name
                            binding.tvProductDescription.text=it.value.description
                            binding.tvPrice.text=it.value.price
                           adapter.submitList(it.value.images)
                            binding.stateView.onSuccess()
                            binding.buttomBar.isVisible=true
                            binding.scrollView3.isVisible=true
//                            if (it.value!=null) {
//                                binding.stateView.onSuccess()
//                            } else {
//                                binding.stateView.onEmpty()
//                            }
                        }
                        is ResultWrapper.Error -> {
                            binding.stateView.onFail()
                            binding.stateView.clickRequest {
                                viewModel.getProduct(args.id)
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