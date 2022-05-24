package com.example.magmarket.ui.categoryfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magmarket.data.model.CategoryItem
import com.example.magmarket.data.model.ProductRecyclerViewItem
import com.example.magmarket.data.repository.CategoryRepository
import com.example.magmarket.utils.Constants
import com.example.magmarket.utils.Constants.ART
import com.example.magmarket.utils.Constants.DIGITAL
import com.example.magmarket.utils.Constants.FASHION_CLOTHING
import com.example.magmarket.utils.Constants.SUPERMARKET
import com.example.magmarket.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val categoryRepository: CategoryRepository) :
    ViewModel() {
    private val _fashionCategory: MutableStateFlow<ResultWrapper<List<CategoryItem>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val fashionCategory = _fashionCategory.asStateFlow()

    private val _digitalCategory: MutableStateFlow<ResultWrapper<List<CategoryItem>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val digitalCategory = _digitalCategory.asStateFlow()

    private val _superMarketCategory: MutableStateFlow<ResultWrapper<List<CategoryItem>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val superMarketCategory = _superMarketCategory.asStateFlow()

    private val _artCategory: MutableStateFlow<ResultWrapper<List<CategoryItem>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val artCategory = _artCategory.asStateFlow()

    init {
        getSubCategories()
    }

    fun getSubCategories() {
        viewModelScope.launch {
            categoryRepository.getSubCategories(FASHION_CLOTHING).collect {
                _fashionCategory.emit(it)
            }
            categoryRepository.getSubCategories(DIGITAL).collect {
                _digitalCategory.emit(it)
            }
            categoryRepository.getSubCategories(SUPERMARKET).collect {
                _superMarketCategory.emit(it)
            }
            categoryRepository.getSubCategories(ART).collect {
                _artCategory.emit(it)
            }
        }
    }
}