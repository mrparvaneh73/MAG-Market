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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.magmarket.R
import com.example.magmarket.adapters.*
import com.example.magmarket.data.model.ProductRecyclerViewItem
import com.example.magmarket.databinding.FragmentHomeBinding
import com.example.magmarket.utils.ResultWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
//    private val bestAdapter = ProductAdapter(clickListener = { productItem ->
//        findNavController().navigate(
//            HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(
//                productItem.id
//            )
//        )
//    }
//
//    )
//    private val newstAdapter = ProductAdapter(clickListener = { productItem ->
//        findNavController().navigate(
//            HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(
//                productItem.id
//            )
//        )
//    }
//
//    )
//    private val mostViewsAdapter = ProductAdapter(clickListener = { productItem ->
//        findNavController().navigate(
//            HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(
//                productItem.id
//            )
//        )
//    }
//
//    )
    private val categoryAdapter=CategoryAdapter()
    private val productadapter = ProductRecyclerviewAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        binding.newestRecyclerview.adapter = productadapter
        binding.categoryRecyclerview.adapter=categoryAdapter
//        binding.mostViewrecyclerview.adapter = mostViewsAdapter
//        binding.bestSellerRecyclerview.adapter = bestAdapter
        val imgs= arrayListOf<Int>(R.drawable.shopingimg,R.drawable.watches,R.drawable.phones)
        val sliderAdapter=HomePagerAdapter(imgs)
        binding.productSlider.adapter=sliderAdapter
        binding.productSlider.clipToPadding=false
        binding.productSlider.clipChildren=false
        binding.productSlider.getChildAt(0).overScrollMode= RecyclerView.OVER_SCROLL_NEVER
        binding.springDotsIndicator.attachTo(binding.productSlider)
        val productItem = mutableListOf<ProductRecyclerViewItem>()
        productItem.add(ProductRecyclerViewItem.HeaderProductTitle(title = R.drawable.newicon))



        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bestProduct.collect {
                    when (it) {
                        is ResultWrapper.Loading -> binding.stateView.onLoading()
                        is ResultWrapper.Success -> {
                            productItem.addAll(it.value)
                            productItem.add(ProductRecyclerViewItem.ShowAll(title = "مشاهده همه "))
                            productadapter.items = productItem

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

        productadapter.itemClickListener = { view, item, position ->
        when(item){
              is ProductRecyclerViewItem.HeaderProductTitle -> {

              }
              is ProductRecyclerViewItem.ProductItem -> {
                  findNavController().navigate(
                      HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(
                          item.id
                      )
                  )
              }
              is ProductRecyclerViewItem.ShowAll -> {

              }
          }

        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categories.collect {
                    when (it) {
                        is ResultWrapper.Loading -> binding.stateView.onLoading()
                        is ResultWrapper.Success -> {
                            Log.d("zzzzzzzz", "onViewCreated: " + it.value.toString())
                            categoryAdapter.submitList(it.value)
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
//        lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.newstProduct.collect {
//                    when (it) {
//                        is ResultWrapper.Loading -> binding.stateView.onLoading()
//                        is ResultWrapper.Success -> {
//                            Log.d("hello", "onViewCreated: " + it.value.toString())
//                            newstAdapter.submitList(it.value)
//                            if (it.value.isNotEmpty()) {
//                                binding.stateView.onSuccess()
//                            } else {
//                                binding.stateView.onEmpty()
//                            }
//                        }
//                        is ResultWrapper.Error -> {
//                            binding.stateView.onFail()
//                            binding.stateView.clickRequest {
//                                viewModel.getNewstProductList()
//                            }
//                            Log.d("errorr", "onViewCreated: " + it.message)
//                            Toast.makeText(
//                                requireActivity(),
//                                it.message,
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//                }
//            }
//        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}