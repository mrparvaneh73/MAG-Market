package com.example.magmarket.ui.homefragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magmarket.data.model.ProductItem
import com.example.magmarket.data.repository.ProductRepository
import com.example.magmarket.utils.Constants.BEST_PRODUCT
import com.example.magmarket.utils.Constants.MOSTVIEW_PRODUCT
import com.example.magmarket.utils.Constants.NEWEST_PRODUCT
import com.example.magmarket.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {

    private val _bestProduct: MutableStateFlow<ResultWrapper<List<ProductItem>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val bestProduct = _bestProduct.asStateFlow()

    private val _newstProduct: MutableStateFlow<ResultWrapper<List<ProductItem>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val newstProduct = _newstProduct.asStateFlow()

    private val _mostViewProduct: MutableStateFlow<ResultWrapper<List<ProductItem>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val mostViewProduct = _mostViewProduct.asStateFlow()

    init {
        getBestProductList()
        getMostViewProductList()
        getNewstProductList()
    }

     fun getBestProductList() {
        viewModelScope.launch {
            val mRos = productRepository.getRemoteProductList(BEST_PRODUCT)
            mRos.collect {
                _bestProduct.emit(it)
            }
        }
    }
    fun getMostViewProductList() {
        viewModelScope.launch {
            val mRos = productRepository.getRemoteProductList(MOSTVIEW_PRODUCT)
            mRos.collect {
                _mostViewProduct.emit(it)
            }
        }
    }
    fun getNewstProductList() {
        viewModelScope.launch {
            val mRos = productRepository.getRemoteProductList(NEWEST_PRODUCT)
            mRos.collect {
                _newstProduct.emit(it)
            }
        }
    }
}