package com.example.magmarket.ui.showmorecomment

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.magmarket.data.remote.model.ProductRecyclerViewItem
import com.example.magmarket.data.remote.model.review.ResponseReview
import com.example.magmarket.data.repository.CategoryRepository
import com.example.magmarket.data.repository.ProductRepository
import javax.inject.Inject

class ShowMoreCommentPaging @Inject constructor(private val productRepository: ProductRepository, val productId:Int) :
    PagingSource<Int, ResponseReview>() {
    override fun getRefreshKey(state: PagingState<Int,ResponseReview>): Int? {
        
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int,ResponseReview> {
        return try {
            val pageNumber = params.key ?: 1
            val response = productRepository.getAllProductComment(page = pageNumber, productId = productId )
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