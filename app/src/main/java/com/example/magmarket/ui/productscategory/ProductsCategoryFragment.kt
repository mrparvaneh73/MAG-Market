package com.example.magmarket.ui.productscategory

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.magmarket.R
import com.example.magmarket.databinding.FragmentProductsCategoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsCategoryFragment: Fragment(R.layout.fragment_products_category) {
  private var _binding:FragmentProductsCategoryBinding?=null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding=FragmentProductsCategoryBinding.bind(view)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}