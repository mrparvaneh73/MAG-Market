package com.example.magmarket.ui.productdetailsfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magmarket.data.model.ProductItem
import com.example.magmarket.data.repository.ProductRepository
import com.example.magmarket.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(private val repository: ProductRepository) : ViewModel() {
    private val _product: MutableStateFlow<ResultWrapper<ProductItem>> =
        MutableStateFlow(ResultWrapper.Loading)
    val product = _product.asStateFlow()

    fun getProduct(id:String){
        viewModelScope.launch {
            repository.getProduct(id).collect{
                _product.emit(it)
            }
        }
    }
}