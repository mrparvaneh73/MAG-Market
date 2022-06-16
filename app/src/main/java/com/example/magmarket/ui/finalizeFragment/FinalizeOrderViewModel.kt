package com.example.magmarket.ui.finalizeFragment

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magmarket.data.datastore.user.User
import com.example.magmarket.data.datastore.user.UserDataStore
import com.example.magmarket.data.remote.Resource
import com.example.magmarket.data.remote.model.coupon.CouponResponse
import com.example.magmarket.data.remote.model.customer.CustomerResponse
import com.example.magmarket.data.remote.model.order.CouponLine
import com.example.magmarket.data.remote.model.order.LineItemX
import com.example.magmarket.data.remote.model.order.ResponseOrder
import com.example.magmarket.data.remote.model.updateorder.UpdateOrder
import com.example.magmarket.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinalizeOrderViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val userDataStore: UserDataStore,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var userId = savedStateHandle.get<Int>("userId")
    var userEmail = ""
    var userName = ""
    var userLastName = ""
    var myorderId = savedStateHandle.get<Int>("myOrderId")
    val couponLines = mutableListOf<CouponLine>()
    var totalCount = 0
    var totalWithoutOff = 0

    init {
        Log.d("inachimishe", ": " + userId + myorderId)
        userId?.let {
            getCustomer(it)
        }


        myorderId?.let {
            getAnOrder(it)
        }

    }

    private val _coupon: MutableStateFlow<Resource<List<CouponResponse>>> =
        MutableStateFlow(Resource.Loading)
    val coupon = _coupon.asStateFlow()

    private val _orderUpdate: MutableSharedFlow<Resource<ResponseOrder>> =
        MutableSharedFlow()
    val orderUpdate = _orderUpdate.asSharedFlow()

    private val _orderList: MutableStateFlow<Resource<ResponseOrder>> =
        MutableStateFlow(Resource.Loading)
    val orderList = _orderList.asStateFlow()

    private val _userFromDataStore: MutableSharedFlow<User> =
        MutableSharedFlow()
    val userFromDataStore = _userFromDataStore.asSharedFlow()

    private val _customer: MutableStateFlow<Resource<CustomerResponse>> =
        MutableStateFlow(Resource.Loading)
    val customer = _customer.asStateFlow()

    private fun getUser() {
        viewModelScope.launch {
            userDataStore.getUser().collect {
                _userFromDataStore.emit(it)
            }
        }

    }

    fun getCustomer(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            cartRepository.getCustomer(id).collect {
                _customer.emit(it)
            }
        }
    }

    fun getAnOrder(orderId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            cartRepository.getAnOrder(orderId).collect {
                _orderList.emit(it)
            }
        }
    }

    fun updateOrderRemote(orderId: Int, order: UpdateOrder) {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.updateOrder(orderId, order).collect {
                _orderUpdate.emit(it)
            }
        }
    }

    fun finalizeOrder() {
        updateOrderRemote(
            orderId = myorderId!!,
            order = UpdateOrder(
                coupon_lines =
                couponLines,
                status =
                "completed"

            )
        )
    }

    fun verifyCoupon(couponCode: String) {
        viewModelScope.launch(Dispatchers.IO) {

            cartRepository.verifyCoupon(couponCode).collect {
                _coupon.emit(it)
            }
        }
    }

    fun saveUser(user: User) {
        viewModelScope.launch {
            userDataStore.saveUser(user)
        }
    }

    fun saveUserWhenFinalizeOrder() {
        saveUser(
            User(
                userId = userId ?: 0,
                email = userEmail ?: "",
                firstName = userName,
                lastName = userLastName,
                myorderId = 0,
                isLogin = true
            )
        )
    }

    fun setPrice(item: LineItemX) {
        totalWithoutOff += (item.subtotal.toInt() * item.quantity)
        totalCount += item.quantity
    }
}