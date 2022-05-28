package com.example.magmarket.ui.productscategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magmarket.data.remote.model.ProductItem
import com.example.magmarket.data.repository.CategoryRepository
import com.example.magmarket.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsCategoryViewModel @Inject constructor(private val categoryRepository:CategoryRepository):ViewModel(){
    private val _productofCategory: MutableStateFlow<ResultWrapper<List<ProductItem>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val productofCategory = _productofCategory.asStateFlow()



    fun getProductofCategory(categoryId:Int){
        viewModelScope.launch {
            categoryRepository.getProductOfCategory(categoryId).collect{
                _productofCategory.emit(it)
            }
        }

    }

}