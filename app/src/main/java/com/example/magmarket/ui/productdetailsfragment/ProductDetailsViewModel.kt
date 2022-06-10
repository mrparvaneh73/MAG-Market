package com.example.magmarket.ui.productdetailsfragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.magmarket.data.local.entities.ProductItemLocal
import com.example.magmarket.data.remote.ResultWrapper
import com.example.magmarket.data.remote.model.ProductItem
import com.example.magmarket.data.remote.model.review.ResponseReview
import com.example.magmarket.data.remote.model.review.Review
import com.example.magmarket.data.repository.ProductRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(private val repository: ProductRepository) :
    ViewModel() {
    var isUserLogin=false
    private val _product: MutableStateFlow<ResultWrapper<ProductItem>> =
        MutableStateFlow(ResultWrapper.Loading)
    val product = _product.asStateFlow()

    private val _similarProducts: MutableStateFlow<ResultWrapper<List<ProductItem>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val similarProducts = _similarProducts.asStateFlow()

    private val _productComment: MutableStateFlow<ResultWrapper<List<ResponseReview>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val productComment = _productComment.asStateFlow()

    private val _responseComment: MutableStateFlow<ResultWrapper<ResponseReview>> =
        MutableStateFlow(ResultWrapper.Loading)
    val responseComment = _responseComment.asStateFlow()

    fun getProduct(id: String) {
        viewModelScope.launch {
            repository.getProduct(id).collect {
                _product.emit(it)
            }
        }
    }

    fun updateOrder(productItemLocal: ProductItemLocal) {
        viewModelScope.launch {
            repository.updateOrder(productItemLocal)
        }
    }

    fun insertProductInOrders(productItemLocal: ProductItemLocal) {
        viewModelScope.launch {
            repository.insertProductToCart(productItemLocal)
        }
    }

    fun isExistInOrders(id: Int) = flow {
        repository.isRowIsExist(id).collect {
            emit(it)
        }
    }

    fun getProductFromOrders(id: Int) = flow {
        repository.getCartProductById(id).collect {
            emit(it)
        }
    }

    fun deletProductFromOrders(productItemLocal: ProductItemLocal) {
        viewModelScope.launch {
            repository.deleteProductFromCart(productItemLocal)
        }
    }

    fun getSimilarProduct(include: String) {
        viewModelScope.launch {
            repository.getSimilarProducts(include).collect{
                _similarProducts.emit(it)
            }
        }
    }

    fun getProductComment(productId: Int) {
        viewModelScope.launch {
            repository.getProductComment(productId).collect {
                _productComment.emit(it)
            }
        }
    }

    fun sendUserComment(review: Review) {
        viewModelScope.launch {
            Log.d("commentmifreste", "sendUserComment: man injam")
            repository.sendUserComment(review).collect{
               _responseComment.emit(it)
            }
        }
    }

    fun getUserFromLocal()= flow {
        repository.getUsersFromLocal().collect{
            emit(it)
        }
    }
}