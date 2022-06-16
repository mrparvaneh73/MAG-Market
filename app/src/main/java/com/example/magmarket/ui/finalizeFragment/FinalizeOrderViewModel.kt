package com.example.magmarket.ui.finalizeFragment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magmarket.data.datastore.user.User
import com.example.magmarket.data.datastore.user.UserDataStore
import com.example.magmarket.data.remote.Resource
import com.example.magmarket.data.remote.model.customer.CustomerResponse
import com.example.magmarket.data.remote.model.order.ResponseOrder
import com.example.magmarket.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinalizeOrderViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val userDataStore: UserDataStore,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
   private var userId = savedStateHandle.get<Int>("userId")
   private var myorderId = savedStateHandle.get<Int>("myOrderId")
    init {
        userId?.let {
            getCustomer(it)
        }
        getUser()

        myorderId?.let {
            getAnOrder(it)
        }

    }
    private val _orderList: MutableStateFlow<Resource<ResponseOrder>> =
        MutableStateFlow(Resource.Loading)
    val orderList = _orderList.asStateFlow()

    private val _userFromDataStore: MutableSharedFlow<User> =
        MutableSharedFlow()
    val userFromDataStore = _userFromDataStore.asSharedFlow()

    private val _customer: MutableStateFlow<Resource<CustomerResponse>> =
        MutableStateFlow(Resource.Loading)
    val customer = _customer.asStateFlow()

  private  fun getUser() {
        viewModelScope.launch {
            userDataStore.getUser().collect {
                _userFromDataStore.emit(it)
            }
        }

    }

    fun getCustomer(id: Int) {
        viewModelScope.launch {
            cartRepository.getCustomer(id).collect {
                _customer.emit(it)
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