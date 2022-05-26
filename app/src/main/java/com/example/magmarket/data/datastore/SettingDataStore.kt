package com.example.magmarket.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.magmarket.utils.Constants.KEY_THEME
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "datastore_settings")
@Singleton
class SettingDataStore @Inject constructor(
    @ApplicationContext context: Context
){
    private val dataStore = context.dataStore

    val preferences: Flow<PreferencesInfo> = dataStore.data.catch { cause ->
    }.map { preference ->
        val theme: Theme = Theme.valueOf(preference[KEY_THEME] ?: Theme.AUTO.name)

        PreferencesInfo(
            theme=theme
        )
    }

    suspend fun updateTheme(theme: Theme) {
        dataStore.edit {
            it[KEY_THEME] = theme.name
        }
    }
}