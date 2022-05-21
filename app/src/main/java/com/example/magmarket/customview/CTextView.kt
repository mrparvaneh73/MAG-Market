package com.example.magmarket.customview

import android.content.Context
import android.util.AttributeSet
import com.example.magmarket.application.App

class CTextView : androidx.appcompat.widget.AppCompatTextView {

    constructor(context: Context) : super(context) {
        setTypeFace()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setTypeFace()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        setTypeFace()
    }

    private fun setTypeFace() {
        val myApplication = context.applicationContext as App
        typeface = myApplication.getTypeFace()
    }
}