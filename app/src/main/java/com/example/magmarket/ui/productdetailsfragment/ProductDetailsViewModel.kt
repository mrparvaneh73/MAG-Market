package com.example.magmarket.ui.productdetailsfragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magmarket.data.datastore.user.UserDataStore
import com.example.magmarket.data.local.entities.ProductItemLocal
import com.example.magmarket.data.local.entities.User
import com.example.magmarket.data.remote.ResultWrapper
import com.example.magmarket.data.remote.model.ProductItem
import com.example.magmarket.data.remote.model.order.LineItem
import com.example.magmarket.data.remote.model.order.Order
import com.example.magmarket.data.remote.model.order.ResponseOrder
import com.example.magmarket.data.remote.model.review.ResponseReview
import com.example.magmarket.data.remote.model.review.Review
import com.example.magmarket.data.remote.model.updateorder.UpdateLineItem
import com.example.magmarket.data.remote.model.updateorder.UpdateOrder
import com.example.magmarket.data.repository.ProductRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(private val repository: ProductRepository,private val userDataStore: UserDataStore) :
    ViewModel() {
    var productId = ""
    var orderId = 0
    var customerId = 0
    var customerEmail = ""
    var customerFirstName = ""
    var customerLastName = ""
    var count = 1
    private val _orderCreate: MutableStateFlow<ResultWrapper<ResponseOrder>> =
        MutableStateFlow(ResultWrapper.Loading)
    val orderCreate = _orderCreate.asStateFlow()

    private val _order: MutableStateFlow<ResultWrapper<ResponseOrder>> =
        MutableStateFlow(ResultWrapper.Loading)
    val order = _order.asStateFlow()

    private val _orderUpdate: MutableStateFlow<ResultWrapper<ResponseOrder>> =
        MutableStateFlow(ResultWrapper.Loading)
    val orderUpdate = _orderUpdate.asStateFlow()

    private val _orderDelete: MutableStateFlow<ResultWrapper<ResponseOrder>> =
        MutableStateFlow(ResultWrapper.Loading)
    val orderDelete = _orderDelete.asStateFlow()

    private val _product: MutableStateFlow<ResultWrapper<ProductItem>> =
        MutableStateFlow(ResultWrapper.Loading)
    val product = _product.asStateFlow()

    private val _productComment: MutableStateFlow<ResultWrapper<List<ResponseReview>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val productComment = _productComment.asStateFlow()

    private val _similarProducts: MutableStateFlow<ResultWrapper<List<ProductItem>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val similarProducts = _similarProducts.asStateFlow()


    fun getProduct() {
        viewModelScope.launch {
            repository.getProduct(productId).collect {
                _product.emit(it)
            }
        }
    }




    fun getUserFromDataStore()= flow {
        userDataStore.getUser().collect{
            emit(it)
        }
    }

    fun saveUserDataStore(user:com.example.magmarket.data.datastore.user.User){
        viewModelScope.launch {
            userDataStore.saveUser(user)
        }

    }






    fun getSimilarProduct(include: String) {
        viewModelScope.launch {
            repository.getSimilarProducts(include).collect {
                _similarProducts.emit(it)
            }
        }
    }

    fun creatOrderRemote(customer_id: Int, order: Order) {
        viewModelScope.launch {
            repository.creatOrder(customer_id, order).collect {
                _orderCreate.emit(it)
            }
        }
    }

    fun updateOrderRemote(orderId: Int, order: UpdateOrder) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.updateOrder(orderId, order).collect {
                _orderUpdate.emit(it)
            }
        }
    }

    fun deleteOrder(orderId: Int) {
        viewModelScope.launch {
            repository.deleteOrder(orderId).collect {
                _orderDelete.emit(it)
            }
        }
    }

    fun getAnOrder(orderId:Int) {
        viewModelScope.launch {

            repository.getAnOrder(orderId).collect {
                _order.emit(it)
            }
        }

    }

    fun deleteAnItemFromOrder(orderId: Int, order: UpdateOrder) {
        viewModelScope.launch {
            repository.deleteAnItemFromOrder(orderId, order)
        }
    }


    fun getProductComment(productId: Int) {
        viewModelScope.launch {
            repository.getProductComment(productId).collect {
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
        orderId = user.orderId
    }


    fun updateAnItemInOrder(number:Int){
        Log.d("productidchand", "updateOrder: "+productId+orderId+count)

        updateOrderRemote(orderId,UpdateOrder(mutableListOf(
            UpdateLineItem(
                id = productId.toInt(),
                quantity = number
            )
        )))
    }
    fun addAnItemInOrder(id:Int){
        Log.d("addorder", "addAnItemInOrder: "+orderId)
        updateOrderRemote(orderId,UpdateOrder(mutableListOf(
            UpdateLineItem(
                product_id = id,
                quantity = 1
            )
        )))

    }

    fun createOrder(){
        creatOrderRemote(customerId,Order(
            mutableListOf(
                LineItem(
                    product_id = productId.toInt(),
                    quantity = count,
                    variation_id = 0
                )
            )
        )
        )
    }
}