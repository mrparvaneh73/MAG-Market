package com.example.magmarket.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.magmarket.R
import com.example.magmarket.databinding.StateviewBinding

class StateView constructor(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {
    private val binding: StateviewBinding
    var text: String = ""
        set(value) {
            field = value
            setStateText()
        }


    init {
        val view = inflate(context, R.layout.stateview, this)
        binding = StateviewBinding.bind(view)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.StateView)
        text = attributes.getString(R.styleable.StateView_state_text) ?:
                attrs.getAttributeResourceValue(R.styleable.StateView_state_text, -1).run {
                    context.resources.getString(this)
                }
        Log.d("StateView", "Log stateViw $text")
        attributes.recycle()
    }

    private fun setStateText() {
        binding.Retry.text = text
    }

    fun onLoading() {
        binding.loadingAnime.isVisible = true
        binding.loadingAnime.playAnimation()
        binding.Retry.isVisible = false
        // binding.tv.visibility= View.GONE
    }

    fun onSuccess() {
        binding.loadingAnime.isVisible = false
        binding.loadingAnime.pauseAnimation()
        binding.Retry.isVisible = false
        binding.emptycart.isVisible=false
    }

    fun onFail() {
        binding.loadingAnime.isVisible = false
        binding.loadingAnime.pauseAnimation()
        binding.Retry.isVisible = true
        text = "Retry" //@resource string
        binding.Retry.isClickable=true
    }

    fun onEmpty() {
        binding.loadingAnime.isVisible = false
        binding.loadingAnime.pauseAnimation()
        binding.Retry.isVisible = true
        binding.emptycart.isVisible=true
        text = "Empty"
        binding.Retry.isClickable = false
    }

    fun clickRequest(request: () -> Unit) {
        binding.Retry.setOnClickListener {
            request()
        }
    }
}