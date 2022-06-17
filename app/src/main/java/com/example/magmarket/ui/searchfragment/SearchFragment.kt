package com.example.magmarket.ui.searchfragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.magmarket.R
import com.example.magmarket.application.Constants
import com.example.magmarket.data.remote.Resource
import com.example.magmarket.databinding.FragmentSearchBinding
import com.example.magmarket.ui.adapters.ProductsOfCategoryAdapter

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel by viewModels<SearchViewModel>()
    private var searchtext: String = ""
    val searchAdapter = ProductsOfCategoryAdapter(clickListener = {
        findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToProductDetailFragment(it.id))

    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)
        backToHome()
        binding.searchView.onActionViewExpanded()
        binding.productRecyclerviw.adapter = searchAdapter
        searchingProduct()
        resultOfSearch()
        sorting()
        responseOfSortingSearch()
    }

    fun searchingProduct() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchtext = query.toString()

                searchViewModel.searchProduct(query!!)


                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

    }

    fun resultOfSearch() {
        searchViewModel.searchResult.collectIt(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {

                    binding.productRecyclerviw.isVisible = false
                }
                is Resource.Success -> {

                    if (it.value.isNotEmpty()) {
                        binding.productRecyclerviw.isVisible = true
                        searchAdapter.submitList(it.value)
                        binding.stateView.onSuccess()
                    } else {
                        binding.stateView.onEmpty()
                        binding.stateView.clickRequest {
                            searchViewModel.searchProduct(searchtext)
                        }
                        Log.d("iamhere", "resultOfSearchempty: ")
                    }
                }
                is Resource.Error -> {
                    Log.d("iamhere", "resultOfSearcherror: ")
                    binding.stateView.onFail()
                    binding.stateView.clickRequest {
                        searchViewModel.searchProduct(searchtext)
                    }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("dobareh", "onCreate: ")
    }

    private fun sorting() = with(binding) {
        sorting.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), sorting)
            popupMenu.menuInflater.inflate(R.menu.sort_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.price_desc -> {
                        searchViewModel.searchText = searchtext
                        searchViewModel.order = Constants.ORDER_DESC
                        searchViewModel.orderBy = Constants.ORDER_BY_PRICE
                        searchViewModel.searchResult()
                    }
                    R.id.price_asc -> {
                        searchViewModel.searchText = searchtext
                        searchViewModel.order = Constants.ORDER_ASC
                        searchViewModel.orderBy = Constants.ORDER_BY_PRICE
                        searchViewModel.searchResult()
                    }
                    R.id.most_newest -> {
                        searchViewModel.searchText = searchtext
                        searchViewModel.order = Constants.ORDER_DESC
                        searchViewModel.orderBy = Constants.ORDER_BY_NEWEST
                        searchViewModel.searchResult()
                    }
                    R.id.most_rating -> {
                        searchViewModel.searchText = searchtext
                        searchViewModel.order = Constants.ORDER_DESC
                        searchViewModel.orderBy = Constants.ORDER_BY_RATING
                        searchViewModel.searchResult()
                    }
                    R.id.most_visiting -> {
                        searchViewModel.searchText = searchtext
                        searchViewModel.order = Constants.ORDER_DESC
                        searchViewModel.orderBy = Constants.ORDER_BY_VISIT
                        searchViewModel.searchResult()
                    }

                }
                true
            }
            popupMenu.show()
        }

    }


   private fun responseOfSortingSearch(){
       searchViewModel.sortingSearch.collectIt(viewLifecycleOwner){
           when (it) {
               is Resource.Loading -> {

                   binding.productRecyclerviw.isVisible = false
               }
               is Resource.Success -> {

                   if (it.value.isNotEmpty()) {
                       binding.productRecyclerviw.isVisible = true
                       searchAdapter.submitList(it.value)
                       binding.stateView.onSuccess()
                   } else {
                       binding.stateView.onEmpty()
                       binding.stateView.clickRequest {
                           searchViewModel.searchProduct(searchtext)
                       }
                       Log.d("iamhere", "resultOfSearchempty: ")
                   }
               }
               is Resource.Error -> {
                   Log.d("iamhere", "resultOfSearcherror: ")
                   binding.stateView.onFail()
                   binding.stateView.clickRequest {
                       searchViewModel.searchProduct(searchtext)
                   }
               }
           }

       }
   }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("dobareh", "onDestroy: ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}