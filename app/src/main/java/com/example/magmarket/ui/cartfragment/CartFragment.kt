package com.example.magmarket.ui.cartfragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.magmarket.R
import com.example.magmarket.data.remote.Resource
import com.example.magmarket.data.remote.model.order.LineItem
import com.example.magmarket.databinding.FragmentCartBinding
import com.example.magmarket.ui.adapters.CartAdapter

import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val nf: NumberFormat = NumberFormat.getInstance(Locale.US)
    private val cartViewModel by activityViewModels<CartViewModel>()
    private val cartAdapter = CartAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCartBinding.bind(view)
        binding.cartRecyclerviw.adapter = cartAdapter
        isUserLogin()

        adapterClickListener()

        collectOnCreate()

        collectWhenUpdate()


    }

    private fun isUserLogin() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.getUser().collect {user->
                    if (user.isLogin) {
                        Log.d("orderidchiye", "isUserLogin: "+user.myorderId)
                        if (user.myorderId != 0) {
                            isNotEmpty()
                            Log.d("orderidchiye", "isUserLogin:chera omadi too ")
                            cartViewModel.orderId = user.myorderId
                            cartViewModel.getAnOrder()

                        }else{
                            isEmpty()
                        }
                        binding.buttonContinue.setOnClickListener {
                            findNavController().navigate(CartFragmentDirections.actionGlobalFinalizeOrderFragment(userId = user.userId, myOrderId = user.myorderId))
                        }
                    } else {
                        openDialog()
                    }

                }
            }
        }
    }

    private fun collectOnCreate() = with(binding) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.orderList.collect {
                    when (it) {
                        is Resource.Loading -> {
                            Log.d("orderidchiye", "collectOnCreate: ")
                            binding.stateView.onLoading()
                            parentDetail.isVisible = false
                            buttomBar.isVisible = false
                            cartRecyclerviw.isVisible=false
                        }
                        is Resource.Success -> {
                            binding.stateView.onSuccess()
                            Log.d("iamhere", "collectOnCreate: ")
                            if (it.value.line_items.isNotEmpty()) {
                                cartAdapter.submitList(it.value.line_items)
                                resetValues()
                                for (i in it.value.line_items) {
                                    cartViewModel.setPrice(i)
                                }
                                init()
                                isNotEmpty()

                            } else {
                                isEmpty()
                            }
                        }
                        is Resource.Error -> {
                            isEmpty()
                            binding.stateView.onFail()
                        }
                    }
                }
            }
        }


    }

    private fun init() = with(binding) {

        tvTotalprice.text = nf.format(cartViewModel.totalPrice)
        tvTotaloffCountPrice.text = nf.format(cartViewModel.totalOff)
        totlapricedesc.text = nf.format(cartViewModel.totalPrice)
        totalPriceWithoutOff.text = nf.format(cartViewModel.totalWithoutOff)
        countproductdesc.text = cartViewModel.totalCount.toString()
    }

    private fun adapterClickListener() {
        cartAdapter.setOnItemClickListener(object : CartAdapter.OnItemClickListener {
            override fun onItemPlus(
                position: Int,
                id: Int,
                quantity: Int,
                image: String,
                regularPrice: String
            ) {
                resetValues()
                var count = quantity
                count++
                cartViewModel.plusOrMinus(id, count, image, regularPrice)
                cartAdapter.notifyItemChanged(position, quantity)


            }

            override fun onItemMinus(
                position: Int,
                id: Int,
                quantity: Int,
                image: String,
                regularPrice: String
            ) {
                resetValues()
                var count = quantity
                count--
                cartViewModel.plusOrMinus(id, count, image, regularPrice)
                if (count == 0) {
                    cartAdapter.notifyItemRemoved(position)
                } else {
                    cartAdapter.notifyItemChanged(position)
                }


            }


        })
    }

    private fun collectWhenUpdate() = with(binding) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.orderUpdate.collectLatest {
                    Log.d("orderidchiye", "collectWhenUpdate: ")
                    when (it) {
                        is Resource.Loading -> {

                        }
                        is Resource.Success -> {
                            if (it.value.line_items.isNotEmpty()) {
                                Log.d("iamhere", "collectWhenUpdate: ")
                                binding.stateView.onSuccess()
                                cartAdapter.submitList(it.value.line_items)
                                for (i in it.value.line_items) {
                                    cartViewModel.setPrice(i)
                                }
                                init()
                                isNotEmpty()
                            } else {
                                isEmpty()
                            }
                        }
                        is Resource.Error -> {
                            binding.stateView.onFail()
                            isEmpty()
                        }
                    }
                }
            }
        }

    }




    fun resetValues() {
        cartViewModel.totalPrice = 0
        cartViewModel.totalCount = 0
        cartViewModel.totalOff = 0
        cartViewModel.totalWithoutOff = 0
    }

    private fun openDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.first_login)
        val button = dialog.findViewById<ImageView>(R.id.img_close)
        val button_login = dialog.findViewById<MaterialButton>(R.id.btn_login)

        button_login.setOnClickListener {
            findNavController().navigate(CartFragmentDirections.actionGlobalUserFragment())
            dialog.dismiss()

        }
        button.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun isEmpty() = with(binding) {
        parentDetail.isVisible = false
        emptycart.isVisible = true
        cartRecyclerviw.isVisible = false
        buttomBar.isVisible = false
    }

    fun isNotEmpty() = with(binding) {
        parentDetail.isVisible = true
        emptycart.isVisible = false
        cartRecyclerviw.isVisible = true
        buttomBar.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}