package com.example.magmarket.ui.cartfragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.magmarket.R
import com.example.magmarket.databinding.FragmentCartBinding
import com.example.magmarket.databinding.FragmentMyOrdersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyOrdersFragment:Fragment(R.layout.fragment_my_orders){
private var _binding: FragmentMyOrdersBinding? = null
private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding=FragmentMyOrdersBinding.bind(view)
    }
}