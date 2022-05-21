package com.example.magmarket.ui.categoryfragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.magmarket.R
import com.example.magmarket.databinding.FragmentCategoryBinding
import com.example.magmarket.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment:Fragment(R.layout.fragment_category) {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCategoryBinding.bind(view)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}