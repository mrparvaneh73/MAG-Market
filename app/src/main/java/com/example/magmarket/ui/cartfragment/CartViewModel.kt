package com.example.magmarket.ui.cartfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magmarket.data.local.entities.OrderList
import com.example.magmarket.data.local.entities.ProductItemLocal
import com.example.magmarket.data.remote.model.order.Order
import com.example.magmarket.data.remote.model.order.ResponseOrder
import com.example.magmarket.data.repository.CartRepository
import com.example.magmarket.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val cartRepository: CartRepository) : ViewModel() {

    private val _orderList: MutableStateFlow<ResultWrapper<ResponseOrder>> =
        MutableStateFlow(ResultWrapper.Loading)
    val orderList = _orderList.asStateFlow()

    private val _order: MutableStateFlow<ResultWrapper<List<ResponseOrder>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val order = _order.asStateFlow()


    fun getOrdersFromLocal() = flow {
        cartRepository.getAllCartProductFromLocal().collect {
            emit(it)
        }

    }


    fun getAllPlacedOrders()= flow {
        cartRepository.getAllPlacedOrders().collect{
            emit(it)
        }
    }

    fun insertPlacedOrdersInLocal(order: OrderList) {
        viewModelScope.launch {
            cartRepository.insertOrder(order)
        }
    }

    fun deleteOrderFromLocal(productItemLocal: ProductItemLocal){
        viewModelScope.launch {
            cartRepository.deleteProductFromCart(productItemLocal)
        }

    }

    fun updateOrder(productItemLocal: ProductItemLocal){
        viewModelScope.launch {
            cartRepository.updateProductCart(productItemLocal)
        }

    }

    fun creatOrder(order: Order) {
        viewModelScope.launch {
            cartRepository.creatOrder(order).collect {
                _orderList.emit(it)
            }
        }

    }

    fun getPlacedOrder(includ: String) {
        viewModelScope.launch {
            cartRepository.getPlacedOrder(includ).collect {
                _order.emit(it)
            }
        }

    }


}