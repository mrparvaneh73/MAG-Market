package com.example.magmarket.data.datastore.theme

import androidx.appcompat.app.AppCompatDelegate

enum class Theme( val mode: Int) {
    LIGHT(AppCompatDelegate.MODE_NIGHT_NO),
    NIGHT(AppCompatDelegate.MODE_NIGHT_YES),
    AUTO(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
}