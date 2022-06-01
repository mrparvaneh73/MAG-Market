package com.example.magmarket.ui.cartfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.magmarket.data.local.entities.ProductItemLocal
import com.example.magmarket.data.remote.model.ProductItem
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




    fun getOrdersFromLocal() = flow {
        cartRepository.getAllCartProductFromLocal().collect {
            emit(it)
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


}