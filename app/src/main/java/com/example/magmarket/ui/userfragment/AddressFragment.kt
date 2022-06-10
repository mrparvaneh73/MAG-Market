package com.example.magmarket.ui.userfragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.magmarket.R
import com.example.magmarket.data.local.entities.User
import com.example.magmarket.data.remote.ResultWrapper
import com.example.magmarket.data.remote.model.customer.Billing
import com.example.magmarket.data.remote.model.customer.Customer
import com.example.magmarket.data.remote.model.customer.Shipping
import com.example.magmarket.databinding.FragmentAdressBinding

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddressFragment : Fragment(R.layout.fragment_adress) {
    private var _binding: FragmentAdressBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<AddressFragmentArgs>()
    private val viewModel by activityViewModels<UserViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAdressBinding.bind(view)
        addAddress()
        getResponseOfEdit()
        backtoAccount()

    }

    private fun addAddress() = with(binding) {
        submitButton.setOnClickListener {
            if (countryTextField.text.isNotEmpty() || cityTextField.text.isNotEmpty() || firstNameTextField.text.isNotEmpty() || secondNameTextField.text.isNotEmpty() || phoneNumberTextField.text.isNotEmpty() || postCodeTextField.text.isNotEmpty() || addressTextField.text.isNotEmpty()) {
                viewModel.updateCustomer(
                    args.id, Customer(
                        billing = Billing(
                            address_1 = addressTextField.text.toString(),
                            address_2 = addressTextField.text.toString(),
                            city = cityTextField.text.toString(),
                            company = "",
                            country = countryTextField.text.toString(),
                            email = args.email,
                            first_name = firstNameTextField.text.toString(),
                            last_name = secondNameTextField.text.toString(),
                            phone = phoneNumberTextField.text.toString(),
                            postcode = postCodeTextField.text.toString(),
                            state = ""
                        ), email = args.email,
                        first_name = firstNameTextField.text.toString(),
                        last_name = secondNameTextField.text.toString(),
                        shipping = Shipping(
                            address_1 = addressTextField.text.toString(),
                            address_2 = addressTextField.text.toString(),
                            city = cityTextField.text.toString(),
                            company = "",
                            country = countryTextField.text.toString(),
                            first_name = firstNameTextField.text.toString(),
                            last_name = secondNameTextField.text.toString(),
                            postcode = phoneNumberTextField.text.toString(),
                            state = "",

                            ), username = ""
                    )
                )
            } else {
                Toast.makeText(
                    requireContext(),
                    "لطفا اطلاعات را بدرستی تکمیل کنید",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getResponseOfEdit() = with(binding) {
        viewModel.userUpdate.collectIt(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {

                }
                is ResultWrapper.Success -> {
                    openDialogSuccess()
                    updateUserLocal(
                        User(
                            userId = args.id,
                            email = args.email,
                            firstName = firstNameTextField.text.toString(),
                            lastName = secondNameTextField.text.toString()
                        )
                    )
                    addressTextField.text = null
                    firstNameTextField.text = null
                    secondNameTextField.text = null
                    countryTextField.text = null
                    cityTextField.text = null
                    secondNameTextField.text = null
                    phoneNumberTextField.text = null
                    postCodeTextField.text = null

                }
                is ResultWrapper.Error -> {
                    Toast.makeText(requireContext(), "مشکلی پیش آمده است", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun openDialogSuccess() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.edit_success)
        val button = dialog.findViewById<ImageView>(R.id.img_close)
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

    private fun backtoAccount() {
        binding.imgBack.setOnClickListener {
            findNavController().navigate(AddressFragmentDirections.actionGlobalUserFragment())
        }
    }

    private fun updateUserLocal(user: User) {
        viewModel.updateUserLocal(user)
    }

}