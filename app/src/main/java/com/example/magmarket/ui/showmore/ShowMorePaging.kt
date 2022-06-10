package com.example.magmarket.ui.showmore

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.magmarket.data.remote.model.ProductRecyclerViewItem
import com.example.magmarket.data.repository.CategoryRepository
import javax.inject.Inject

class ShowMorePaging @Inject constructor(private val categoryRepository: CategoryRepository,val orderBy:String) :
    PagingSource<Int, ProductRecyclerViewItem.ProductItem>() {
    override fun getRefreshKey(state: PagingState<Int, ProductRecyclerViewItem.ProductItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductRecyclerViewItem.ProductItem> {
        return try {
            val pageNumber = params.key ?: 1
            val response = categoryRepository.getShowmoreProduct(page = pageNumber, orderBy = orderBy)
            val result=response.body()
            if (response.isSuccessful && result !=null){

                LoadResult.Page(
                    data =result!!,
                    prevKey = if(pageNumber==1) null else pageNumber.minus(1),
                    nextKey = if (result.isEmpty()) null else pageNumber.plus(1)

                )
            }else{
               throw Exception("Response Not Found")
            }

        }catch (e:Exception){
            LoadResult.Error(e)
        }


    }
}