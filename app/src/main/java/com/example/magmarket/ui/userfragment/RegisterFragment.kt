package com.example.magmarket.ui.userfragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.magmarket.R
import com.example.magmarket.data.remote.Resource
import com.example.magmarket.data.remote.model.customer.Billing
import com.example.magmarket.data.remote.model.customer.Customer
import com.example.magmarket.data.remote.model.customer.Shipping
import com.example.magmarket.databinding.FragmentRegisterBinding

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<UserViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegisterBinding.bind(view)
       createCustomer()

        backToUserFragment()
    }

    fun createCustomer() = with(binding) {

        registerButton.setOnClickListener {
            val email = emailTextField.text.toString()
            val firstname = firstNameTextField.text.toString()
            val lastname = secondNameTextField.text.toString()
            if (emailTextField.text.length == 0 || firstNameTextField.text.length == 0 || secondNameTextField.text.length == 0) {
                Toast.makeText(
                    requireContext(),
                    "لطفا تمام اطلاعات را وارد کنید ",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.createCustomer(
                    Customer(
                        billing = Billing(
                            "",
                            "",
                            "",
                            "",
                            "",
                            email = email,
                            first_name = firstname,
                            last_name = lastname,
                            phone = "",
                            "",
                            ""
                        ),
                        email = email,
                        first_name = firstname,
                        last_name = lastname,
                        shipping = Shipping(
                            "", "", "", "", "", first_name = firstname, last_name = lastname, "", ""
                        ),
                        username = email
                    )
                )
            }
            responsOfRegister()
        }
    }

    fun responsOfRegister()= with(binding) {
        viewModel.customerIdResponse.collectIt(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    openDialogSuccess(it.value.id.toString())
                    emailTextField.text=null
                    firstNameTextField.text=null
                    secondNameTextField.text=null

                }
                is Resource.Error -> {
                    openDialogfailure()
                }
            }
        }
    }

    private fun openDialogSuccess(userId: String) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.success_register)
        val button = dialog.findViewById<ImageView>(R.id.img_close)
        val userNumber = dialog.findViewById<TextView>(R.id.user_number)
        userNumber.text = userId
        button.setOnClickListener {
            dialog.dismiss()

        }
        dialog.show()
    }

    private fun openDialogfailure() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.failed_register)
        val button = dialog.findViewById<ImageView>(R.id.img_close)

        button.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
private fun backToUserFragment(){
    binding.imgBack.setOnClickListener {
        findNavController().navigate(RegisterFragmentDirections.actionGlobalUserFragment())

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}