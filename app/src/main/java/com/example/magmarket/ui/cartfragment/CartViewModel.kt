package com.example.magmarket.ui.cartfragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magmarket.data.datastore.user.UserDataStore
import com.example.magmarket.data.remote.Resource
import com.example.magmarket.data.remote.model.Cart
import com.example.magmarket.data.remote.model.ProductItem
import com.example.magmarket.data.remote.model.customer.CustomerResponse
import com.example.magmarket.data.remote.model.order.LineItemX
import com.example.magmarket.data.remote.model.order.Order
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
    var cart: MutableList<Cart> = mutableListOf()
    var orderId = 0
    var lineItem = mutableListOf<LineItemX>()
    val productItems = mutableListOf<ProductItem>()

    private val _remoteProducts: MutableStateFlow<Resource<List<ProductItem>>> =
        MutableStateFlow(Resource.Loading)
    val remoteProducts = _remoteProducts.asStateFlow()

    private val _orderUpdate: MutableStateFlow<Resource<ResponseOrder>> =
        MutableStateFlow(Resource.Loading)
    val orderUpdate = _orderUpdate.asStateFlow()

    private val _orderList: MutableSharedFlow<Resource<ResponseOrder>> =
        MutableSharedFlow()
    val orderList = _orderList.asSharedFlow()

    private val _customer: MutableStateFlow<Resource<CustomerResponse>> =
        MutableStateFlow(Resource.Loading)
    val customer = _customer.asStateFlow()

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

    fun creatOrder(customer_id: Int, order: Order) {
        viewModelScope.launch {
            cartRepository.creatOrder(customer_id, order).collect {
//                _orderList.emit(it)
            }
        }
    }

    fun getPlacedOrder(customer_id: Int) {
        viewModelScope.launch {
            cartRepository.getPlacedOrder(customer_id).collect {
                _order.emit(it)
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

    fun getAnOrder() {
        viewModelScope.launch {
            cartRepository.getAnOrder(orderId).collect {
                _orderList.emit(it)
            }
        }
    }

//    fun getProductFromRemote(include: String) {
//        viewModelScope.launch {
//            cartRepository.getProductFromRemote(include).collect {
//                _remoteProducts.emit(it)
//
//            }
//        }
//    }

    fun updateOrderRemote(orderId: Int, order: UpdateOrder) {
        viewModelScope.launch(Dispatchers.Default) {
            cartRepository.updateOrder(orderId, order).collect {
                _orderUpdate.emit(it)
            }
        }
    }

    fun productIdFromLineItem() {
        val temp = mutableListOf(0)
        for (i in lineItem) {
            temp.add(i.product_id)

        }
//        Log.d("productssss", "productIdFromLineItem: " + temp.toString())
//        getProductFromRemote(temp.toString())
    }

//    fun myCart(): List<Cart> {
//        val temp = mutableListOf<Cart>()
//        for (i in lineItem) {
//            for (j in productItems) {
//                if (i.product_id == j.id.toInt()) {
//                    temp.add(
//                        Cart(
//                            id = i.id,
//                            productId = i.product_id,
//                            name = j.name ?: "محصول فاقد نام",
//                            price = j.price ?: "محصول فاقد قیمت",
//                            images = j.images?.get(0)!!.src,
//                            regular_price = j.regular_price ?: "",
//                            sale_price = j.sale_price ?: "",
//                            count = i.quantity
//                        )
//                    )
//                }
//            }
//        }
//        return temp
//    }

    fun plus(productId: Int) {
        val tempt = mutableListOf<UpdateLineItem>()
//        for (i in myCart()) {
//            if (i.productId == productId) {
//
//                tempt.add(
//                    UpdateLineItem(
//                        id = i.id,
//                        product_id = i.productId,
//                        quantity = i.count.plus(1),
//                    )
//                )
//
//
//            } else {
//
//                tempt.add(
//                    UpdateLineItem(
//                        id = i.id,
//                        product_id = i.productId,
//                        quantity = i.count
//                    )
//                )
//
//
//            }
//        }
        updateOrderRemote(orderId, UpdateOrder(tempt))
    }

    fun minus(productId: Int) {
        val tempt = mutableListOf<UpdateLineItem>()
//        for (i in myCart()) {
//            if (i.productId == productId) {
//
//                tempt.add(
//                    UpdateLineItem(
//                        id = i.id,
//                        product_id = i.productId,
//                        quantity = i.count.minus(1),
//                    )
//                )
//
//
//            } else {
//
//                tempt.add(
//                    UpdateLineItem(
//                        id = i.id,
//                        product_id = i.productId,
//                        quantity = i.count
//                    )
//                )
//
//
//            }
//        }
        updateOrderRemote(orderId, UpdateOrder(tempt))
    }
}