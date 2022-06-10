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
import com.example.magmarket.data.remote.ResultWrapper
import com.example.magmarket.databinding.FragmentMyOrdersBinding
import com.example.magmarket.ui.adapters.OrderPlacedAdapter
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyOrdersFragment : Fragment(R.layout.fragment_my_orders) {
    private var _binding: FragmentMyOrdersBinding? = null
    private val binding get() = _binding!!
    private val placedOrderId= mutableListOf<Int>(0)
    private val cartViewModel by activityViewModels<CartViewModel>()
    val orderPlacedAdapter = OrderPlacedAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMyOrdersBinding.bind(view)
        binding.rcOrderplaced.adapter = orderPlacedAdapter
        isUserLogin()
        collect()
    }


    private fun isUserLogin() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.getUserFromLocal().collect {
                    if (it.userId !=0) {
                        cartViewModel.getPlacedOrder(it.userId)
                    } else {
                        openDialog()
                    }
                }
            }
        }
    }
    private fun openDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.first_login)
        val button = dialog.findViewById<ImageView>(R.id.img_close)
        val button_login = dialog.findViewById<MaterialButton>(R.id.btn_login)

        button_login.setOnClickListener {
    findNavController().navigate(MyOrdersFragmentDirections.actionGlobalUserFragment())
            dialog.dismiss()
        }
        button.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun collect() {
        cartViewModel.order.collectIt(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {
                    binding.stateView.onLoading()
                    binding.rcOrderplaced.isVisible = false
                }
                is ResultWrapper.Success -> {
                    Log.d("responseordersman ", "collect: "+it.value)
                    orderPlacedAdapter.submitList(it.value)
                    binding.rcOrderplaced.isVisible = true
                    if (it.value.isNotEmpty()) {
                        binding.stateView.onSuccess()
                    } else {
                        binding.stateView.onEmpty()
                    }
                }
                is ResultWrapper.Error -> {
                    binding.stateView.onFail()

                }
            }
        }
    }

    private fun <T> StateFlow<T>.collectIt(lifecycleOwner: LifecycleOwner, function: (T) -> Unit) {
        lifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collect {
                    function.invoke(it)
                }
            }
        }
    }
}