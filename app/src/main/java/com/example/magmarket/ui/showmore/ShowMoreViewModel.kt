package com.example.magmarket.ui.showmore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magmarket.data.model.ProductRecyclerViewItem
import com.example.magmarket.data.repository.CategoryRepository
import com.example.magmarket.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowMoreViewModel@Inject constructor(private val categoryRepository: CategoryRepository):ViewModel() {
    private val _showmore: MutableStateFlow<ResultWrapper<List<ProductRecyclerViewItem.ProductItem>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val showmore = _showmore.asStateFlow()
    fun getShowmore(orderBy:String){
        viewModelScope.launch {
            categoryRepository.getShowmoreProduct(orderBy).collect{
                _showmore.emit(it)
            }
        }
    }
}