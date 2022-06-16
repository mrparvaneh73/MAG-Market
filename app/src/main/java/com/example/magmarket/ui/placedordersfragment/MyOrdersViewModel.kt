package com.example.magmarket.ui.placedordersfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magmarket.data.datastore.user.User
import com.example.magmarket.data.datastore.user.UserDataStore
import com.example.magmarket.data.remote.Resource
import com.example.magmarket.data.remote.model.order.ResponseOrder
import com.example.magmarket.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyOrdersViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val userDataStore: UserDataStore
) : ViewModel() {
    var userId = 0
    private val _placedOrders: MutableStateFlow<Resource<List<ResponseOrder>>> =
        MutableStateFlow(Resource.Loading)
    val placedOrders = _placedOrders.asStateFlow()


    private val _orderList: MutableSharedFlow<Resource<ResponseOrder>> =
        MutableSharedFlow()
    val orderList = _orderList.asSharedFlow()

    private val _userFromDataStore: MutableSharedFlow<User> =
        MutableSharedFlow()
    val userFromDataStore = _userFromDataStore.asSharedFlow()

    init {
        getUser()
    }

    fun getPlacedOrder() {
        viewModelScope.launch {
            cartRepository.getPlacedOrder(userId).collect {
                _placedOrders.emit(it)
            }
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            userDataStore.getUser().collect {
                _userFromDataStore.emit(it)
            }
        }
    }


    fun getAnOrder(orderId:Int) {
        viewModelScope.launch {
            cartRepository.getAnOrder(orderId).collect {
                _orderList.emit(it)
            }
        }
    }
}