package com.example.magmarket.ui.productdetailsfragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.magmarket.R
import com.example.magmarket.databinding.FragmentProductDetailBinding
import com.example.magmarket.databinding.FragmentSendCommentBinding

class SendCommentFragment :Fragment(R.layout.fragment_send_comment) {
    private var _binding: FragmentSendCommentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<ProductDetailsViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= FragmentSendCommentBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}