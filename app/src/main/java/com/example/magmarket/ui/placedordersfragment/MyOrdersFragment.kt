package com.example.magmarket.ui.placedordersfragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.magmarket.R
import com.example.magmarket.data.remote.Resource
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

    private val cartViewModel by viewModels<MyOrdersViewModel>()
    private val orderPlacedAdapter = OrderPlacedAdapter(clickListener = {
        findNavController().navigate(MyOrdersFragmentDirections.actionGlobalEachPlacedOrderDetailsFragment(it.id))
    }

    )
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMyOrdersBinding.bind(view)
        binding.rcOrderplaced.adapter = orderPlacedAdapter
        getUser()
        getPlasedOrder()
    }

    private fun getUser() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.userFromDataStore.collect {
                    if (it.isLogin) {
                        cartViewModel.userId=it.userId
                        cartViewModel.getPlacedOrder()
                    } else {
                        openDialog()
                    }
                }
            }
        }
    }

    fun getPlasedOrder() = with(binding) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.placedOrders.collect {
                    when (it) {
                        is Resource.Loading -> {
                            stateView.onLoading()
                            rcOrderplaced.isVisible = false
                        }
                        is Resource.Success -> {
                            stateView.onSuccess()
                            orderPlacedAdapter.submitList(it.value)
                            rcOrderplaced.isVisible = true
                        }
                        is Resource.Error -> {

                        }
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