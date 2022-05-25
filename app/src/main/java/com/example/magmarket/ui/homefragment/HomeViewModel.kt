package com.example.magmarket.ui.homefragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magmarket.data.model.CategoryItem
import com.example.magmarket.data.model.ProductRecyclerViewItem
import com.example.magmarket.data.repository.ProductRepository
import com.example.magmarket.utils.Constants.BEST_PRODUCT
import com.example.magmarket.utils.Constants.MOSTVIEW_PRODUCT
import com.example.magmarket.utils.Constants.NEWEST_PRODUCT
import com.example.magmarket.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {

    private val _bestProduct: MutableStateFlow<ResultWrapper<List<ProductRecyclerViewItem.ProductItem>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val bestProduct = _bestProduct.asStateFlow()

    private val _newstProduct: MutableStateFlow<ResultWrapper<List<ProductRecyclerViewItem.ProductItem>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val newstProduct = _newstProduct.asStateFlow()

    private val _mostViewProduct: MutableStateFlow<ResultWrapper<List<ProductRecyclerViewItem.ProductItem>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val mostViewProduct = _mostViewProduct.asStateFlow()

    private val _categories: MutableStateFlow<ResultWrapper<List<CategoryItem>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val categories = _categories.asStateFlow()

    init {
        getAllProducts()
    }

    fun getAllProducts() {
        viewModelScope.launch {
            val bestProduct = async {
                productRepository.getRemoteProductList(BEST_PRODUCT).collect {
                    _bestProduct.emit(it)
                }
            }
            val mostView = async {
                productRepository.getRemoteProductList(MOSTVIEW_PRODUCT).collect {
                    _mostViewProduct.emit(it)
                }
            }
            val newestProduct = async {
                productRepository.getRemoteProductList(NEWEST_PRODUCT).collect {
                    _newstProduct.emit(it)
                }
            }
            val allCategory = async {

                productRepository.getAllCategories().collect {
                    _categories.emit(it)
                }
            }
            bestProduct.await()
            mostView.await()
            newestProduct.await()
            allCategory.await()


        }

    }


}