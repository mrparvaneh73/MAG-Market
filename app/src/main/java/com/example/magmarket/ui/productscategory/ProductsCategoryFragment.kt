package com.example.magmarket.ui.productscategory

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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.magmarket.R
import com.example.magmarket.adapters.ProductsOfCategoryAdapter
import com.example.magmarket.adapters.ShowMoreAdapter
import com.example.magmarket.databinding.FragmentProductsCategoryBinding
import com.example.magmarket.utils.ResultWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductsCategoryFragment : Fragment(R.layout.fragment_products_category) {
    private var _binding: FragmentProductsCategoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProductsCategoryViewModel>()

    private val args by navArgs<ProductsCategoryFragmentArgs>()
    private val categoryProductAdapter = ProductsOfCategoryAdapter(clickListener = { productItem ->
        findNavController().navigate(
            ProductsCategoryFragmentDirections.actionProductsCategoryFragmentToProductDetailFragment(
                productItem.id
            )
        )
    }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductsCategoryBinding.bind(view)
        Log.d("idfroosh", "onViewCreated: "+args.category)
        backPressed()
        collect()
        init()
    }

    private fun init() = with(binding) {
        searchbox.imgsearch.setImageResource(R.drawable.back)
        categoryProductAdapter.stateRestorationPolicy=RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        productRecyclerviw.adapter = categoryProductAdapter
        viewModel.getProductofCategory(args.category)

    }

    private fun collect() {
        viewModel.productofCategory.collectIt(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {

                    binding.stateView.onLoading()
                }
                is ResultWrapper.Success -> {

                    categoryProductAdapter.submitList(it.value)

                    if (it.value.isNotEmpty()) {
                        binding.stateView.onSuccess()
                    } else {
                        binding.stateView.onEmpty()
                    }
                }
                is ResultWrapper.Error -> {
                    binding.stateView.onFail()
                    binding.stateView.clickRequest {
                        viewModel.getProductofCategory(args.category)
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
private fun backPressed(){
    binding.searchbox.imgsearch.setOnClickListener {
        requireActivity().onBackPressed()
    }
}
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}