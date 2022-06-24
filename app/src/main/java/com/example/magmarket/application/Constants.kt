package com.example.magmarket.application


import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.magmarket.BuildConfig


object Constants {
    const val FASHION_CLOTHING=62
    const val DIGITAL=52
    const val SUPERMARKET=81
    const val ART=76
    const val CHANNEL_ID_1="FIRSTCHANNEL"
    const val CHANNEL_NAME="NEWPRODUCT"
    const val WORK_NAME="newproduct"
    const val TAG="mynewproduct"
    val KEY_THEME = stringPreferencesKey("preferences_them")
    const val BASE_URL = "https://woocommerce.maktabsharif.ir/wp-json/wc/v3/"
    const val ORDER_DESC="desc"
    const val ORDER_ASC="asc"
    const val ORDER_BY_PRICE="price"
    const val ORDER_BY_NEWEST="date"
    const val ORDER_BY_VISIT="popularity"
    const val ORDER_BY_RATING="rating"
    const val NEWEST_PRODUCT = "date"
    const val MOSTVIEW_PRODUCT = "rating"
    const val BEST_PRODUCT = "popularity"
    var BASE_PARAM:HashMap<String,String> = hashMapOf("consumer_key" to  "ck_e4ff7a8003d76ffe7cb50068a72b1122fd2c0222" , "consumer_secret" to "cs_cd9e2c13b2591adacdd56328c0ac82b1c8c23340")


}