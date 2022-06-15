package com.example.magmarket.ui.showmorecomment

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.magmarket.data.remote.ResultWrapper
import com.example.magmarket.data.remote.model.ProductItem
import com.example.magmarket.data.repository.ProductRepository
import com.example.magmarket.ui.showmoreproduct.ShowMorePaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowMoreCommentViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var productId = savedStateHandle.get<Int>("porductIdForComment")

    private val _product: MutableStateFlow<ResultWrapper<ProductItem>> =
        MutableStateFlow(ResultWrapper.Loading)
    val product = _product.asStateFlow()

    init {

        productId?.let { getProduct(it.toString()) }

    }

    fun getProduct(id: String) {
        Log.d("idchiye", "getProduct: " + productId)
        viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            productRepository.getProduct(id).collect {
                _product.emit(it)
            }
        }
    }
    fun getAllComment(productId: Int) = Pager(PagingConfig(pageSize = 20)) {
        ShowMoreCommentPaging(productRepository, productId)
    }.flow.cachedIn(viewModelScope)
}