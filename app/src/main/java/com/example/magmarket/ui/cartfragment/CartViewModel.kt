package com.example.magmarket.ui.cartfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magmarket.data.datastore.user.UserDataStore
import com.example.magmarket.data.remote.Resource
import com.example.magmarket.data.remote.model.ProductItem
import com.example.magmarket.data.remote.model.order.LineItemX
import com.example.magmarket.data.remote.model.order.MetaData
import com.example.magmarket.data.remote.model.order.ResponseOrder
import com.example.magmarket.data.remote.model.updateorder.UpdateLineItem
import com.example.magmarket.data.remote.model.updateorder.UpdateOrder
import com.example.magmarket.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val userDataStore: UserDataStore
) : ViewModel() {

    var orderId = 0
    var totalPrice = 0
    var totalOff = 0
    var totalCount = 0
    var totalWithoutOff = 0


    private val _remoteProducts: MutableStateFlow<Resource<List<ProductItem>>> =
        MutableStateFlow(Resource.Loading)
    val remoteProducts = _remoteProducts.asStateFlow()

    private val _orderUpdate: MutableSharedFlow<Resource<ResponseOrder>> =
        MutableSharedFlow()
    val orderUpdate = _orderUpdate.asSharedFlow()

    private val _orderList: MutableSharedFlow<Resource<ResponseOrder>> =
        MutableSharedFlow()
    val orderList = _orderList.asSharedFlow()



    private val _order: MutableStateFlow<Resource<List<ResponseOrder>>> =
        MutableStateFlow(Resource.Loading)
    val order = _order.asStateFlow()

    private val _orderRemote: MutableStateFlow<Resource<ResponseOrder>> =
        MutableStateFlow(Resource.Loading)
    val orderRemote = _orderRemote.asStateFlow()

    var isSuccess: Boolean = false


    fun getUser() = flow {
        userDataStore.getUser().collect {
            emit(it)
        }
    }



    fun getPlacedOrder(customer_id: Int) {
        viewModelScope.launch {
            cartRepository.getPlacedOrder(customer_id).collect {
                _order.emit(it)
            }
        }

    }


    fun getAnOrder() {
        viewModelScope.launch {
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


    fun plusOrMinus(id: Int,quantity:Int,image:String,regularPrice:String) {

        updateOrderRemote(orderId, UpdateOrder( line_items = mutableListOf(
            UpdateLineItem(
                id = id,
                quantity = quantity,
                meta_data = mutableListOf(
                    MetaData(
                        key = "image",
                        value = image
                    ),
                    MetaData(
                        key = "price",
                        value = regularPrice
                    )
                )
            )
        ), status = "pending"))
    }

//    fun minus(id: Int,quantity:Int,image:String,regularPrice:String) {
//
//        updateOrderRemote(orderId, UpdateOrder(line_items =  mutableListOf(
//            UpdateLineItem(
//                id = id,
//                quantity = quantity,
//                meta_data = mutableListOf(
//                    MetaData(
//                        key = "image",
//                        value = image
//                    ),
//                    MetaData(
//                        key = "price",
//                        value = regularPrice
//                    )
//                )
//            )
//        )))
//    }

    fun setPrice(item:LineItemX){
        totalPrice += (item.subtotal.toInt() * item.quantity)
        totalOff += (item.meta_data[1].value.toInt()
            .minus(item.subtotal.toInt()) * item.quantity)
        totalWithoutOff += (item.meta_data[1].value.toInt() * item.quantity)
       totalCount += item.quantity
    }
}