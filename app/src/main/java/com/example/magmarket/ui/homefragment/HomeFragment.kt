package com.example.magmarket.ui.homefragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.magmarket.R
import com.example.magmarket.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}