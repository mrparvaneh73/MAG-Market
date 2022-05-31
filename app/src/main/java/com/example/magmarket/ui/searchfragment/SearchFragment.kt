package com.example.magmarket.ui.searchfragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.magmarket.R
import com.example.magmarket.databinding.FragmentSearchBinding
import com.example.magmarket.ui.adapters.ProductsOfCategoryAdapter
import com.example.magmarket.utils.ResultWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel by viewModels<SearchViewModel>()
    val searchAdapter = ProductsOfCategoryAdapter(clickListener = {

    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)
        backToHome()
        binding.searchView.onActionViewExpanded()
        binding.productRecyclerviw.adapter = searchAdapter
        searchingProduct()
        resultOfSearch()
    }

    fun searchingProduct() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchViewModel.searchProduct(query!!)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    fun resultOfSearch() {
        searchViewModel.searchResult.collectIt(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {

                }
                is ResultWrapper.Success -> {
                    searchAdapter.submitList(it.value)

                    if (it.value.isNotEmpty()) {

                    } else {

                    }
                }
                is ResultWrapper.Error -> {


                }
            }
        }
    }

    fun backToHome() {
        binding.imgBack.setOnClickListener {
            requireActivity().onBackPressed()
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