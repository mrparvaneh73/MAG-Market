package com.example.magmarket.ui.userfragment

import android.os.Bundle
import android.view.View
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
import com.example.magmarket.data.datastore.Theme
import com.example.magmarket.data.remote.ResultWrapper
import com.example.magmarket.databinding.FragmentUserBinding

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserFragment : Fragment(R.layout.fragment_user) {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private var userid: Int = 0
    private var userEmail: String = ""
    private var userFirstName: String = ""
    private var userLastName:String=""
    private val viewModel by activityViewModels<UserViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUserBinding.bind(view)
        init()
        navigateToSignUp()
        getUser()
      loginFromLocal()
        exitFromAccount()
        editUser()

    }

    private fun init() = with(binding) {
        viewModel.islight.observe(viewLifecycleOwner) { isLight ->
            if (isLight == true) {
                rbLight.isChecked = true
            } else if (isLight == false) {
                rbDark.isChecked = true
            }
        }
        settingTheme.setOnCheckedChangeListener { _, checkedId ->
            val theme = when (checkedId) {
                rbLight.id -> Theme.LIGHT
                rbDark.id -> Theme.NIGHT
                else -> Theme.AUTO
            }
            viewModel.updateTheme(theme)
        }
    }

    private fun login(id: Int) {
        viewModel.getCustomer(id)
    }

    private fun getUser() = with(binding) {
        loginButton?.setOnClickListener {
            if (passwordTextField.text.isNotEmpty() || emailTextField!!.text.isNotEmpty()) {
                login(passwordTextField.text.toString().toInt())

            } else {
                Toast.makeText(
                    requireContext(),
                    "لطفا اطلاعات را به درستی وارد کنید",
                    Toast.LENGTH_SHORT
                ).show()
            }
            getResponseLogin()

        }
    }

    private fun getResponseLogin() {
        viewModel.user.collectIt(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {

                }
                is ResultWrapper.Success -> {
                    if (binding.emailTextField!!.text.toString() == it.value.email) {
                        binding.viewLogin!!.isVisible = false
                        binding.viewAccountinfo!!.isVisible = true
                        binding.tvFullNameRegistered!!.text =
                            "${it.value.first_name} ${it.value.last_name}"
                        binding.tvEmailRegistered!!.text = it.value.email
//                        viewModel.insertUserToLocal(
//                            User(
//                                it.value.id,
//                                it.value.email,
//                                it.value.first_name,
//                                it.value.last_name
//                            )
//                        )
                        viewModel.saveUser(com.example.magmarket.data.datastore.user.User(
                            userId =it.value.id,
                            email = it.value.email,
                           firstName = it.value.first_name,
                            lastName = it.value.last_name,
                            isLogin = true
                        ))
                        userid = it.value.id
                        userEmail = it.value.email

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "کاربری با این مشخصات یافت نشد ",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.emailTextField!!.text = null
                        binding.passwordTextField.text = null
                    }
                }
                is ResultWrapper.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "کاربری با این مشخصات یافت نشد",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun loginFromLocal() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getUser().collect {
                    if (it.isLogin){
                        binding.viewLogin!!.isVisible = false
                        binding.viewAccountinfo!!.isVisible = true
                        binding.tvFullNameRegistered!!.text =
                            "${it.firstName} ${it.lastName}"
                        binding.tvEmailRegistered!!.text = it.email
                        userid = it.userId
                        userEmail = it.email
                        userFirstName =it.firstName
                        userLastName=it.lastName
                    }else{
                        binding.viewLogin!!.isVisible = true
                        binding.viewAccountinfo!!.isVisible = false
                    }


                }
            }
        }

    }


    private fun navigateToSignUp() {
        binding.SignUpButton.setOnClickListener {
            findNavController().navigate(UserFragmentDirections.actionGlobalRegisterFragment())
        }

    }

    private fun exitFromAccount() {
        binding.exitFromAccount!!.setOnClickListener {
            binding.viewLogin!!.isVisible = true
            binding.viewAccountinfo!!.isVisible = false
         viewModel.saveUser(com.example.magmarket.data.datastore.user.User(
             isLogin = false,
             myorderId = 0

         ))
        }
    }
private fun editUser(){
    binding.edit!!.setOnClickListener {
        findNavController().navigate(UserFragmentDirections.actionGlobalAddressFragment(id=userid, firstname = userFirstName, lastname = userLastName, email = userEmail))
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