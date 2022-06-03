package com.example.magmarket.ui.categoryfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magmarket.data.remote.model.CategoryItem
import com.example.magmarket.data.repository.CategoryRepository
import com.example.magmarket.application.Constants.ART
import com.example.magmarket.application.Constants.DIGITAL
import com.example.magmarket.application.Constants.FASHION_CLOTHING
import com.example.magmarket.application.Constants.SUPERMARKET
import com.example.magmarket.data.remote.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
            val fashionClothing = async {

                categoryRepository.getSubCategories(FASHION_CLOTHING).collect {
                    _fashionCategory.emit(it)
                }

            }
            val digital = async {

                categoryRepository.getSubCategories(DIGITAL).collect {
                    _digitalCategory.emit(it)
                }

            }
            val supermarket = async {
                categoryRepository.getSubCategories(SUPERMARKET).collect {
                    _superMarketCategory.emit(it)
                }
            }


            val art = async {
                categoryRepository.getSubCategories(ART).collect {
                    _artCategory.emit(it)
                }
            }
            fashionClothing.await()
            digital.await()
            supermarket.await()
            art.await()
        }
    }
}