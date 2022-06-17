package com.example.magmarket.ui.searchfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magmarket.data.remote.Resource
import com.example.magmarket.data.remote.model.ProductItem
import com.example.magmarket.data.repository.ProductRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {
    private val _searchResult: MutableStateFlow<Resource<List<ProductItem>>> =
        MutableStateFlow(Resource.Loading)
    val searchResult = _searchResult.asStateFlow()

    private val _sortingSearch: MutableStateFlow<Resource<List<ProductItem>>> =
        MutableStateFlow(Resource.Loading)
    val sortingSearch = _sortingSearch.asStateFlow()
    var searchText = ""
    var order = ""
    var orderBy=""

    fun searchProduct(search: String) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.getSearchProduct(search).collect {
                _searchResult.emit(it)
            }
        }
    }

    fun searchingSortedProduct(
        order: String?,
        orderBy: String?,
        search: String?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.searchingSortedProduct(order, orderBy, search).collect {
                _sortingSearch.emit(it)
            }
        }

    }

    fun searchResult(){
        searchingSortedProduct(order,orderBy,searchText)
    }
}