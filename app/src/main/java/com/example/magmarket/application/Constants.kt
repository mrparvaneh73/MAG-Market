package com.example.magmarket.application


import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.magmarket.BuildConfig


object Constants {
    const val FASHION_CLOTHING=62
    const val DIGITAL=52
    const val SUPERMARKET=81
    const val ART=76
    val KEY_THEME = stringPreferencesKey("preferences_them")
    const val CHANNEL_ID_1="FIRSTCHANNEL"
    const val CHANNEL_NAME="NEWPRODUCT"
    const val WORK_NAME="newproduct"
    const val TAG="mynewproduct"
    const val BASE_URL = "https://woocommerce.maktabsharif.ir/wp-json/wc/v3/"
    const val NEWEST_PRODUCT = "date"
    const val MOSTVIEW_PRODUCT = "rating"
    const val BEST_PRODUCT = "popularity"
    var BASE_PARAM:HashMap<String,String> = hashMapOf("consumer_key" to  BuildConfig.CONSUMER_KEY , "consumer_secret" to BuildConfig.CONSUMER_SECRET)


}