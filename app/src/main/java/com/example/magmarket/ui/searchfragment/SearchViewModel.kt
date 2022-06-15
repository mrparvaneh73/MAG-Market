package com.example.magmarket.ui.searchfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magmarket.data.remote.Resource
import com.example.magmarket.data.remote.model.ProductItem
import com.example.magmarket.data.repository.ProductRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val productRepository: ProductRepository) :ViewModel() {
    private val _searchResult: MutableStateFlow<Resource<List<ProductItem>>> =
        MutableStateFlow(Resource.Loading)
    val searchResult = _searchResult.asStateFlow()


    fun searchProduct(search:String){
        viewModelScope.launch {
            productRepository.getSearchProduct(search).collect{
                _searchResult.emit(it)
            }
        }
    }
}