package com.example.magmarket.ui.finalizeFragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import com.example.magmarket.data.remote.model.order.Billing
import com.example.magmarket.data.remote.model.order.LineItem
import com.example.magmarket.data.remote.model.order.Shipping
import com.example.magmarket.databinding.FragmentFinalizeorderBinding

import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FinalizeOrderFragment : Fragment(R.layout.fragment_finalizeorder) {
    private var _binding: FragmentFinalizeorderBinding? = null
    private val binding get() = _binding!!

    var successId: Int = 0
    private val finalizeViewModel by activityViewModels<FinalizeOrderViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFinalizeorderBinding.bind(view)
//        getUser()
        getCustomerInfo()

        back()

    }

//    fun getUser() {
//        lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                finalizeViewModel.userFromDataStore.collect {
//                    if (it.isLogin) {
//                        finalizeViewModel.getCustomer(it.userId)
//                        if (it.myorderId != 0) {
//                            finalizeViewModel.orderId = it.myorderId
//                            finalizeViewModel.getAnOrder()
//                        }
//                    }
//
//                }
//            }
//        }
//    }

    fun getCustomerInfo() = with(binding) {
        finalizeViewModel.customer.collectIt(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.stateView.onLoading()
                    binding.parent.isVisible = false

                }
                is Resource.Success -> {
                    binding.stateView.onSuccess()
                    binding.parent.isVisible = true

                    if (it.value.billing.address_1.isNotEmpty()) {
                        fullnameReciver.text = "${it.value.first_name} ${it.value.last_name}"
                        addressReciver.text = it.value.billing.address_1
                        phonenumberReciver.text = it.value.billing.phone

                    } else {
                        binding.parent.isVisible = false
                        openDialogaddAdress()
                    }
                }
                is Resource.Error -> {

                }
            }
        }
    }

//  private  fun finalizeOrder(customerId: Int, order: Order) {
//        binding.btnFinalize.setOnClickListener {
//            cartViewModel.creatOrder(customerId, order)
//            ResponseOfOrder()
//        }
//    }



    private fun ResponseOfOrder() {
        finalizeViewModel.orderList.collectIt(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {

                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "somethingwentwrong", Toast.LENGTH_SHORT)
                        .show()

                }
            }
        }
    }

    private fun back() {
        binding.backImg.setOnClickListener {
            findNavController().navigate(
                FinalizeOrderFragmentDirections.actionGlobalParentOfCartFragment(
                    successId
                )
            )
        }
    }

    private fun openDialog(orderId: String) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.order_placed)
        val button = dialog.findViewById<ImageView>(R.id.img_close)
        val orderNumber = dialog.findViewById<TextView>(R.id.order_number)
        orderNumber.text = orderId
        button.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun openDialogaddAdress() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.add_address_dialog)
        val button = dialog.findViewById<ImageView>(R.id.img_close)
        val button_login = dialog.findViewById<MaterialButton>(R.id.btn_complete_info)

        button_login.setOnClickListener {

            findNavController().navigate(FinalizeOrderFragmentDirections.actionGlobalUserFragment())
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
