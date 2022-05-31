package com.example.magmarket.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.magmarket.ui.cartfragment.CartFragment
import com.example.magmarket.ui.cartfragment.MyOrdersFragment

class CartViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle):
    FragmentStateAdapter(fragmentManager,lifecycle) {

    override fun getItemCount(): Int =2

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return CartFragment()
            1 -> return MyOrdersFragment()
            else -> return CartFragment()
        }

    }
}