package com.example.magmarket.ui.productdetailsfragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.magmarket.R
import com.example.magmarket.databinding.FragmentProductDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment: Fragment(R.layout.fragment_product_detail) {
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductDetailBinding.bind(view)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}