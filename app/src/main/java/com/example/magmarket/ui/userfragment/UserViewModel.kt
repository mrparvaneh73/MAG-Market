package com.example.magmarket.ui.userfragment

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magmarket.data.datastore.SettingDataStore
import com.example.magmarket.data.datastore.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val settingDataStore: SettingDataStore) :
    ViewModel() {
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

    fun check() {
        viewModelScope.launch {
            settingDataStore.preferences.collect {
                val mode = it.theme.mode
                val currentMode = AppCompatDelegate.getDefaultNightMode()
                if (currentMode != mode) {
                    _islight.postValue(true)
                }else{
                    _islight.postValue(false)
                }
            }

        }

    }
}