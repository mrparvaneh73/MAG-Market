package com.example.magmarket.ui.mainactivity

import androidx.lifecycle.ViewModel
import com.example.magmarket.data.datastore.theme.SettingDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor( private val settingDataStore: SettingDataStore):ViewModel(){
    val preferences = settingDataStore.preferences
}