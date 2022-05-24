package com.example.magmarket.ui.homefragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()

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
    private val categoryAdapter = CategoryAdapter()
    private val bestAdapter = ProductRecyclerviewAdapter()
    private val newestAdapter = ProductRecyclerviewAdapter()
    private val mostViewAdapter = ProductRecyclerviewAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        init()


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
        collectCategory()
        collect()
        clickListener()
    }

    fun init() {
        binding.newestRecyclerview.adapter = newestAdapter
        binding.categoryRecyclerview.adapter = categoryAdapter
        binding.bestSellerRecyclerview.adapter = bestAdapter
        binding.mostViewrecyclerview.adapter = mostViewAdapter
        val imgs = arrayListOf<Int>(R.drawable.shopingimg, R.drawable.watches, R.drawable.phones)
        val sliderAdapter = HomePagerAdapter(imgs)
        binding.productSlider.adapter = sliderAdapter
        binding.productSlider.clipToPadding = false
        binding.productSlider.clipChildren = false
        binding.productSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        binding.springDotsIndicator.attachTo(binding.productSlider)
    }

    fun clickListener() {

        adapterClickListener(bestAdapter)
        adapterClickListener(newestAdapter)
        adapterClickListener(mostViewAdapter)
    }

    fun collectCategory() {
        viewModel.categories.collectIt(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> binding.stateView.onLoading()
                is ResultWrapper.Success -> {
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
                        viewModel.getAllProducts()
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

    fun collect() {

        viewModel.bestProduct.collectIt(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {
                    binding.stateView.onLoading()
                    binding.mainviewgroup.isVisible = false
                }
                is ResultWrapper.Success -> {
                    val bestItem = mutableListOf<ProductRecyclerViewItem>()
                    bestItem.add(ProductRecyclerViewItem.HeaderProductTitle(title = R.drawable.best))
                    bestItem.addAll(it.value)
                    bestItem.add(ProductRecyclerViewItem.ShowAll(title = "مشاهده همه "))
                    bestAdapter.items = bestItem
                    binding.mainviewgroup.isVisible = true
                    if (it.value.isNotEmpty()) {
                        binding.stateView.onSuccess()
                    } else {
                        binding.stateView.onEmpty()
                    }
                }
                is ResultWrapper.Error -> {
                    binding.stateView.onFail()
                    binding.stateView.clickRequest {
                        viewModel.getAllProducts()
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
        viewModel.newstProduct.collectIt(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {
                    binding.stateView.onLoading()
                    binding.mainviewgroup.isVisible = false
                }
                is ResultWrapper.Success -> {
                    val newestItem = mutableListOf<ProductRecyclerViewItem>()
                    newestItem.add(ProductRecyclerViewItem.HeaderProductTitle(title = R.drawable.newicon))
                    newestItem.addAll(it.value)
                    newestItem.add(ProductRecyclerViewItem.ShowAll(title = "مشاهده همه "))
                    newestAdapter.items = newestItem
                    binding.mainviewgroup.isVisible = true
                    if (it.value.isNotEmpty()) {
                        binding.stateView.onSuccess()
                    } else {
                        binding.stateView.onEmpty()
                    }
                }
                is ResultWrapper.Error -> {
                    binding.stateView.onFail()
                    binding.stateView.clickRequest {
                        viewModel.getAllProducts()
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
        viewModel.mostViewProduct.collectIt(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {
                    binding.stateView.onLoading()
                    binding.mainviewgroup.isVisible = false
                }
                is ResultWrapper.Success -> {
                    val mostViewItem = mutableListOf<ProductRecyclerViewItem>()
                    mostViewItem.add(ProductRecyclerViewItem.HeaderProductTitle(title = R.drawable.most))
                    mostViewItem.addAll(it.value)
                    mostViewItem.add(ProductRecyclerViewItem.ShowAll(title = "مشاهده همه "))
                    mostViewAdapter.items = mostViewItem
                    binding.mainviewgroup.isVisible = true
                    if (it.value.isNotEmpty()) {
                        binding.stateView.onSuccess()
                    } else {
                        binding.stateView.onEmpty()
                    }
                }
                is ResultWrapper.Error -> {
                    binding.stateView.onFail()
                    binding.stateView.clickRequest {
                        viewModel.getAllProducts()
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

    fun <T> StateFlow<T>.collectIt(lifecycleOwner: LifecycleOwner, function: (T) -> Unit) {
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

    fun adapterClickListener(adapter: ProductRecyclerviewAdapter) {
        adapter.itemClickListener = { view, item, position ->
            when (item) {
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
    }
}
