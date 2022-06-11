package com.example.magmarket.data.datastore.user

data class User(val userId:Int=0,val email:String="",val firstName:String="",val lastName:String="",
val orderId:Int=0,val orderStatus:String="",val isLogin:Boolean)
