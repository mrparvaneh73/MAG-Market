package com.example.magmarket.ui.homefragment

import android.os.Bundle
import android.view.View
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
import com.example.magmarket.ui.adapters.CategoryAdapter
import com.example.magmarket.ui.adapters.HomePagerAdapter
import com.example.magmarket.ui.adapters.ProductRecyclerviewAdapter
import com.example.magmarket.data.model.ProductRecyclerViewItem
import com.example.magmarket.databinding.FragmentHomeBinding
import com.example.magmarket.utils.Constants.BEST_PRODUCT
import com.example.magmarket.utils.Constants.MOSTVIEW_PRODUCT
import com.example.magmarket.utils.Constants.NEWEST_PRODUCT
import com.example.magmarket.utils.ResultWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()

    private val categoryAdapter = CategoryAdapter(clickListener = { categoryItem ->
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToProductsCategoryFragment(
                categoryItem.id
            )
        )
    })

    private val bestAdapter = ProductRecyclerviewAdapter()

    private val newestAdapter = ProductRecyclerviewAdapter()

    private val mostViewAdapter = ProductRecyclerviewAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        init()
        collectCategory()
        collect()
        clickListener()


    }

    private fun init() {
        newestAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        categoryAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        bestAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        mostViewAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.newestRecyclerview.adapter = newestAdapter
        binding.categoryRecyclerview.adapter = categoryAdapter
        binding.bestSellerRecyclerview.adapter = bestAdapter
        binding.mostViewrecyclerview.adapter = mostViewAdapter
        val images = arrayListOf(
            R.drawable.onlinbuying,
            R.drawable.shopingimg,
            R.drawable.watches,
            R.drawable.phones
        )
        val sliderAdapter = HomePagerAdapter(images)
        binding.productSlider.adapter = sliderAdapter
        binding.productSlider.clipToPadding = false
        binding.productSlider.clipChildren = false
        binding.productSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        binding.springDotsIndicator.attachTo(binding.productSlider)
    }

    private fun clickListener() {

        adapterClickListener(bestAdapter, BEST_PRODUCT, "بهترین محصولات")
        adapterClickListener(newestAdapter, NEWEST_PRODUCT, "جدیدترین محصولات")
        adapterClickListener(mostViewAdapter, MOSTVIEW_PRODUCT, "پربازدیدترین محصولات")
    }

    private fun collectCategory() {
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

                }
            }
        }
    }

    fun collect() {

        viewModel.bestProduct.collectIt(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {
                    binding.stateView.onLoading()
                    binding.scrollview.isVisible = false
                }
                is ResultWrapper.Success -> {
                    val bestItem = mutableListOf<ProductRecyclerViewItem>()
                    bestItem.add(ProductRecyclerViewItem.HeaderProductTitle(title = R.drawable.bestproduct))
                    bestItem.addAll(it.value)
                    bestItem.add(ProductRecyclerViewItem.ShowAll(title = "مشاهده همه "))
                    bestAdapter.items = bestItem
                    binding.scrollview.isVisible = true
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

                }
            }
        }
        viewModel.newstProduct.collectIt(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {
                    binding.stateView.onLoading()
                    binding.scrollview.isVisible = false
                }
                is ResultWrapper.Success -> {
                    val newestItem = mutableListOf<ProductRecyclerViewItem>()
                    newestItem.add(ProductRecyclerViewItem.HeaderProductTitle(title = R.drawable.newproduct))
                    newestItem.addAll(it.value)
                    newestItem.add(ProductRecyclerViewItem.ShowAll(title = "مشاهده همه "))
                    newestAdapter.items = newestItem
                    binding.scrollview.isVisible = true
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

                }
            }
        }
        viewModel.mostViewProduct.collectIt(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {
                    binding.stateView.onLoading()
                    binding.scrollview.isVisible = false
                    binding.parentsearchbox.isVisible = false
                }
                is ResultWrapper.Success -> {
                    val mostViewItem = mutableListOf<ProductRecyclerViewItem>()

                    mostViewItem.add(ProductRecyclerViewItem.HeaderProductTitle(title = R.drawable.mostwatched))
                    mostViewItem.addAll(it.value)
                    mostViewItem.add(ProductRecyclerViewItem.ShowAll(title = "مشاهده همه "))
                    mostViewAdapter.items = mostViewItem
                    binding.scrollview.isVisible = true
                    binding.parentsearchbox.isVisible = true
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

    private fun adapterClickListener(
        adapter: ProductRecyclerviewAdapter,
        orderBy: String,
        orderByName: String
    ) {
        adapter.itemClickListener = { _, item, _ ->
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
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToShowMoreFragment(
                            orderBy, orderByName
                        )
                    )
                }
            }

        }
    }


}
