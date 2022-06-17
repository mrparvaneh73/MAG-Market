package com.example.magmarket.ui.finalizeFragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.magmarket.R
import com.example.magmarket.data.datastore.user.User

import com.example.magmarket.data.remote.Resource
import com.example.magmarket.data.remote.model.order.*
import com.example.magmarket.data.remote.model.updateorder.UpdateOrder
import com.example.magmarket.databinding.FragmentFinalizeorderBinding

import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*
import kotlin.math.log

@AndroidEntryPoint
class FinalizeOrderFragment : Fragment(R.layout.fragment_finalizeorder) {
    private var _binding: FragmentFinalizeorderBinding? = null
    private val binding get() = _binding!!
    private val nf: NumberFormat = NumberFormat.getInstance(Locale.US)
    private val finalizeViewModel by viewModels<FinalizeOrderViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFinalizeorderBinding.bind(view)
        getUser()
        ResponseOfOrder()
        getCustomerInfo()
        submitCoupon()
        getResponseOfSubmitCoupon()
        finalizeOrder()
        reponseFinal()
        back()

    }

    private fun init() = with(binding) {

        countproductdesc.text = finalizeViewModel.totalCount.toString()
        totalPriceWithoutOff.text = nf.format(finalizeViewModel.totalWithoutOff)
        totlapriceWithCoupone.text = nf.format(finalizeViewModel.totalWithoutOff)
    }

    fun getUser() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                finalizeViewModel.userFromDataStore.collect {
                    if (it.isLogin) {
                        finalizeViewModel.userEmail = it.email
                        finalizeViewModel.userName = it.firstName
                        finalizeViewModel.userLastName = it.lastName
                    }

                }
            }
        }
    }

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

    private fun finalizeOrder() {
        binding.btnFinalize.setOnClickListener {
            finalizeViewModel.finalizeOrder()

        }
    }

    private fun reponseFinal() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                finalizeViewModel.orderUpdate.collect {
                    when (it) {
                        is Resource.Loading -> {
                            binding.stateView.onLoading()
                            binding.parent.isVisible = false

                        }
                        is Resource.Success -> {
                            binding.stateView.onSuccess()
                            binding.parent.isVisible = true
                            if (it.value.status == "completed") {

                                openDialog(it.value.id.toString())
                                finalizeViewModel.saveUserWhenFinalizeOrder()

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
        }

    }


    private fun ResponseOfOrder() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                finalizeViewModel.orderList.collect {
                    when (it) {
                        is Resource.Loading -> {

                        }
                        is Resource.Success -> {
                            for (i in it.value.line_items) {
                                finalizeViewModel.setPrice(i)
                            }
                            init()
                        }
                        is Resource.Error -> {
                            Toast.makeText(
                                requireContext(),
                                "somethingwentwrong",
                                Toast.LENGTH_SHORT
                            )
                                .show()

                        }
                    }
                }
            }
        }

    }

    private fun submitCoupon() {
        binding.btnSubmitCoupon.setOnClickListener {
            if (binding.edCoupon.text.isNotEmpty()) {
                finalizeViewModel.verifyCoupon(binding.edCoupon.text.toString())
            } else {
                binding.edCoupon.error = "please add a code"
            }
        }

    }

    fun getResponseOfSubmitCoupon() = with(binding) {
        finalizeViewModel.coupon.collectIt(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    if (it.value.isNotEmpty()) {
                        acceptCoupon.isVisible = true
                        ignoreCoupon.isVisible = false
                        finalizeViewModel.couponLines.add(
                            CouponLine(
                                code = it.value[0].code,
                                discount = it.value[0].amount,
                                discount_tax = it.value[0].amount
                            )
                        )
                        Log.d(
                            "Sss",
                            "getResponseOfSubmitCoupon: " + it.value[0].amount.toFloat()
                                .toInt() + finalizeViewModel.totalWithoutOff
                        )
                        tvTotaloffCountPrice.text = it.value[0].amount
                        totlapriceWithCoupone.text = nf.format(
                            finalizeViewModel.totalWithoutOff - ((finalizeViewModel.totalWithoutOff * it.value[0].amount.toFloat()
                                .toInt()) / 100)
                        )
                    } else {
                        tvTotaloffCountPrice.text = ""
                        totlapriceWithCoupone.text =
                            nf.format(finalizeViewModel.totalWithoutOff.toInt())
                        acceptCoupon.isVisible = false
                        ignoreCoupon.isVisible = true
                        finalizeViewModel.couponLines.clear()
                    }


                }
                is Resource.Error -> {
                    acceptCoupon.isVisible = false
                    ignoreCoupon.isVisible = true
                }
            }
        }
    }

    private fun back() {
        binding.backImg.setOnClickListener {
            findNavController().navigate(
                FinalizeOrderFragmentDirections.actionGlobalParentOfCartFragment(

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
