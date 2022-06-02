package com.example.magmarket.ui.cartfragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.navArgs
import com.example.magmarket.R
import com.example.magmarket.databinding.FragmentCartBinding
import com.example.magmarket.databinding.FragmentParentOfCartBinding
import com.example.magmarket.ui.adapters.CartViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ParentOfCartFragment : Fragment(R.layout.fragment_parent_of_cart) {
    private var tabtitle = arrayOf("سبد خرید", "خرید های من ")
    private var _binding: FragmentParentOfCartBinding? = null
    private val args by navArgs<ParentOfCartFragmentArgs>()
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentParentOfCartBinding.bind(view)
        binding.viewpager.adapter = CartViewPagerAdapter(childFragmentManager, lifecycle)
        TabLayoutMediator(binding.tablayout, binding.viewpager) { tab, position ->
            tab.text = tabtitle[position]
        }.attach()
    }

    fun successId(): Int {
        return args.successId
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}