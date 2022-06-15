package com.example.magmarket.ui.showmoreproduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.magmarket.data.remote.ResultWrapper
import com.example.magmarket.data.remote.model.ProductRecyclerViewItem
import com.example.magmarket.data.repository.CategoryRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ShowMoreViewModel @Inject constructor(private val categoryRepository: CategoryRepository) :
    ViewModel() {

    fun getShowmore(orderBy: String) = Pager(PagingConfig(pageSize = 20)) {

        ShowMorePaging(categoryRepository, orderBy)
    }.flow.cachedIn(viewModelScope)


}