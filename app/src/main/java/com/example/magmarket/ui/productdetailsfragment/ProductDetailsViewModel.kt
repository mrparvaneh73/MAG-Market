package com.example.magmarket.ui.productdetailsfragment

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magmarket.data.datastore.user.UserDataStore
import com.example.magmarket.data.remote.ResultWrapper
import com.example.magmarket.data.remote.model.ProductItem
import com.example.magmarket.data.remote.model.order.LineItem
import com.example.magmarket.data.remote.model.order.MetaData
import com.example.magmarket.data.remote.model.order.Order
import com.example.magmarket.data.remote.model.order.ResponseOrder
import com.example.magmarket.data.remote.model.review.ResponseReview
import com.example.magmarket.data.remote.model.review.Review
import com.example.magmarket.data.remote.model.updateorder.UpdateLineItem
import com.example.magmarket.data.remote.model.updateorder.UpdateOrder
import com.example.magmarket.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val userDataStore: UserDataStore,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    var productId = savedStateHandle.getLiveData<String>("idOfProduct").value
    var id = 0

    init {

        productId?.let { getProduct(it) }

    }

    var productImage = ""
    var orderId = 0
    var customerId = 0
    var customerEmail = ""
    var customerFirstName = ""
    var customerLastName = ""
    var count = 1

    private val _orderCreate: MutableStateFlow<ResultWrapper<ResponseOrder>> =
        MutableStateFlow(ResultWrapper.Loading)
    val orderCreate = _orderCreate.asStateFlow()

    private val _order: MutableSharedFlow<ResultWrapper<ResponseOrder>> =
        MutableSharedFlow()
    val order = _order.asSharedFlow()

    private val _orderUpdate: MutableSharedFlow<ResultWrapper<ResponseOrder>> =
        MutableSharedFlow()
    val orderUpdate = _orderUpdate.asSharedFlow()


    private val _product: MutableStateFlow<ResultWrapper<ProductItem>> =
        MutableStateFlow(ResultWrapper.Loading)
    val product = _product.asStateFlow()

    private val _productComment: MutableStateFlow<ResultWrapper<List<ResponseReview>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val productComment = _productComment.asStateFlow()

    private val _similarProducts: MutableStateFlow<ResultWrapper<List<ProductItem>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val similarProducts = _similarProducts.asStateFlow()


    fun getProduct(id: String) {
        Log.d("idchiye", "getProduct: " + productId)
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

    fun saveUserDataStore(user: com.example.magmarket.data.datastore.user.User) {
        viewModelScope.launch {
            userDataStore.saveUser(user)
        }

    }


    fun getSimilarProduct(include: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSimilarProducts(include).collect {
                _similarProducts.emit(it)
            }
        }
    }

    fun creatOrderRemote(customer_id: Int, order: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.creatOrder(customer_id, order).collect {
                _orderCreate.emit(it)
            }
        }
    }

    fun updateOrderRemote(orderId: Int, order: UpdateOrder) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateOrder(orderId, order).collect {
                _orderUpdate.emit(it)
            }
        }
    }


    fun getAnOrder(myorderId: Int) {
        Log.d("getorder", "responseGetAnOrder:" +orderId)
        viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            repository.getAnOrder(myorderId).collect {
                _order.emit(it)
            }
        }

    }


    fun getProductComment() {
        viewModelScope.launch {
            repository.getProductComment(productId!!.toInt()).collect {
                _productComment.emit(it)
            }
        }
    }

    fun sendUserComment(review: Review) {
        viewModelScope.launch {
            repository.sendUserComment(review)
        }
    }

    fun setUserInfo(user: com.example.magmarket.data.datastore.user.User) {
        customerId = user.userId
        customerEmail = user.email
        customerFirstName = user.firstName
        customerLastName = user.lastName
        orderId = user.myorderId
    }


    fun updateAnItemInOrder(number: Int) {


        updateOrderRemote(
            orderId, UpdateOrder(
                mutableListOf(
                    UpdateLineItem(
                        id = id,
                        quantity = number,
                        meta_data = mutableListOf(
                            MetaData(
                                key = "image",
                                value = productImage
                            )
                        )
                    )
                )
            )
        )
    }

    fun addAnItemInOrder() {
        Log.d("addorder", "addAnItemInOrder: " + orderId)
        updateOrderRemote(
            orderId, UpdateOrder(
                mutableListOf(
                    UpdateLineItem(
                        product_id = productId!!.toInt(),
                        quantity = 1,
                        meta_data = mutableListOf(
                            MetaData(
                                key = "image",
                                value = productImage
                            )
                        )


                    )
                )
            )
        )

    }

    fun createOrder() {
        creatOrderRemote(
            customerId, Order(
                mutableListOf(
                    LineItem(
                        product_id = productId!!.toInt(),
                        quantity = count,
                        variation_id = 0,
                        meta_data = mutableListOf(
                            MetaData(
                                key = "image",
                                value = productImage
                            )
                        )
                    )
                )
            )
        )
    }
}