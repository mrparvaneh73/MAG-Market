package com.example.magmarket.ui.sendcommentfragment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magmarket.data.datastore.user.UserDataStore
import com.example.magmarket.data.remote.Resource
import com.example.magmarket.data.remote.model.ProductItem
import com.example.magmarket.data.remote.model.review.ResponseReview
import com.example.magmarket.data.remote.model.review.Review
import com.example.magmarket.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendCommentViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val userDataStore: UserDataStore,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var sendCommentProductId = savedStateHandle.get<String>("sencommentproductid")
    init {

        sendCommentProductId?.let { getProduct(it) }

    }
    private val _product: MutableStateFlow<Resource<ProductItem>> =
        MutableStateFlow(Resource.Loading)
    val product = _product.asStateFlow()

    private val _responseComment: MutableSharedFlow<Resource<ResponseReview>> =
        MutableSharedFlow()
    val responseComment = _responseComment.asSharedFlow()

  private  fun getProduct(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            repository.getProduct(id).collect {
                _product.emit(it)
            }
        }
    }

    fun getUserFromDataStore() = flow {
        userDataStore.getUser().collect {
            emit(it)
        }
    }

    fun sendUserComment(review: Review) {
        viewModelScope.launch {
            repository.sendUserComment(review).collect{
                _responseComment.emit(it)
            }
        }
    }
}