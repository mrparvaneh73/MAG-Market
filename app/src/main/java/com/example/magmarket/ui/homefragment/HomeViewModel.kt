package com.example.magmarket.ui.homefragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magmarket.data.remote.model.CategoryItem
import com.example.magmarket.data.remote.model.ProductItem
import com.example.magmarket.data.remote.model.ProductRecyclerViewItem
import com.example.magmarket.data.repository.ProductRepository
import com.example.magmarket.application.Constants.BEST_PRODUCT
import com.example.magmarket.application.Constants.MOSTVIEW_PRODUCT
import com.example.magmarket.application.Constants.NEWEST_PRODUCT
import com.example.magmarket.data.remote.ResultWrapper

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {
    private val _slider: MutableStateFlow<ResultWrapper<ProductItem>> =
        MutableStateFlow(ResultWrapper.Loading)
    val slider = _slider.asStateFlow()


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
        getProduct()
    }

    fun getAllProducts() {
        viewModelScope.launch {
            val bestProduct = async {
                productRepository.getRemoteProductList(1,BEST_PRODUCT).collect {
                    _bestProduct.emit(it)
                }
            }
            val mostView = async {
                productRepository.getRemoteProductList(1,MOSTVIEW_PRODUCT).collect {
                    _mostViewProduct.emit(it)
                }
            }
            val newestProduct = async {
                productRepository.getRemoteProductList(1,NEWEST_PRODUCT).collect {
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




    fun getProduct(id: String="608") {
        viewModelScope.launch {
            productRepository.getProduct(id).collect {
                _slider.emit(it)
            }
        }
    }


}