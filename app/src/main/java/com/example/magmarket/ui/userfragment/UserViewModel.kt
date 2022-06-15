package com.example.magmarket.ui.userfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magmarket.data.datastore.SettingDataStore
import com.example.magmarket.data.datastore.Theme
import com.example.magmarket.data.datastore.user.User
import com.example.magmarket.data.datastore.user.UserDataStore

import com.example.magmarket.data.remote.Resource
import com.example.magmarket.data.remote.model.customer.Customer
import com.example.magmarket.data.remote.model.customer.CustomerResponse
import com.example.magmarket.data.repository.UserRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val settingDataStore: SettingDataStore,
    private val userRepository: UserRepository,
    private val userDataStore: UserDataStore
) :
    ViewModel() {
    private val _customerIdResponse: MutableStateFlow<Resource<CustomerResponse>> =
        MutableStateFlow(Resource.Loading)
    val customerIdResponse = _customerIdResponse.asStateFlow()

    private val _user: MutableStateFlow<Resource<CustomerResponse>> =
        MutableStateFlow(Resource.Loading)
    val user = _user.asStateFlow()

    private val _userUpdate: MutableStateFlow<Resource<CustomerResponse>> =
        MutableStateFlow(Resource.Loading)
    val userUpdate = _userUpdate.asStateFlow()

    private val _userFromDataStore: MutableStateFlow<User> =
        MutableStateFlow(User(isLogin = false))
    val userFromDataStore = _userFromDataStore.asSharedFlow()

    private val _islight = MutableLiveData<Boolean>()
    val islight: LiveData<Boolean> = _islight
    fun updateTheme(theme: Theme) {
        viewModelScope.launch {
            settingDataStore.updateTheme(theme)
        }
    }

    init {
        check()
    }

    fun createCustomer(customer: Customer) {
        viewModelScope.launch {
            userRepository.createCustomer(customer).collect {
                _customerIdResponse.emit(it)
            }
        }

    }

    fun getCustomer(id: Int) {
        viewModelScope.launch {
            userRepository.getCustomer(id).collect {
                _user.emit(it)
            }
        }
    }

    fun updateCustomer(id: Int, customer: Customer) {
        viewModelScope.launch {
            userRepository.updateCustomer(id, customer).collect {
                _userUpdate.emit(it)
            }
        }
    }

    fun check() {
        viewModelScope.launch {
            settingDataStore.preferences.collect {
                val mode = it.theme.mode
                if (mode == 2) {
                    _islight.value = false
                } else if (mode == 1) {
                    _islight.value = true
                }
            }

        }

    }

    fun saveUser(user: User) {
        viewModelScope.launch {
            userDataStore.saveUser(user)
        }
    }
    init {
        getUser()
    }

    fun getUser() {
        viewModelScope.launch {
            userDataStore.getUser().collect{
                _userFromDataStore. emit(it)
            }
        }

    }
//    fun getUserFromLocal()= flow {
//        userRepository.getUsersFromLocal().collect{
//            emit(it)
//        }
//    }

//    fun insertUserToLocal(user: User) {
//        viewModelScope.launch {
//            userRepository.insertUser(user)
//        }
//    }


//    fun deleteUserFromLocal(user: User){
//        viewModelScope.launch {
//            userRepository.deleteUser(user)
//        }
//
//    }

//    fun updateUserLocal(user: User){
//        viewModelScope.launch {
//            userRepository.updateUserLocal(user)
//        }
//
//    }


}