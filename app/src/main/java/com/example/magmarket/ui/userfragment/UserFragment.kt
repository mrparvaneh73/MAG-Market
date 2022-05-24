package com.example.magmarket.ui.userfragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.magmarket.R
import com.example.magmarket.data.datastore.Theme
import com.example.magmarket.databinding.FragmentUserBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment(R.layout.fragment_user) {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<UserViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUserBinding.bind(view)
        init()
//        viewModel.islight.observe(viewLifecycleOwner){
//            if (it==true){
//                binding.rbDark.isChecked=true
//            }else{
//                binding.rbLight.isChecked=true
//            }
//        }
    }

   private fun init() = with(binding) {
        settingTheme.setOnCheckedChangeListener { radioGroup, checkedId ->
            val theme = when (checkedId) {
                rbLight.id -> Theme.LIGHT
                rbDark.id -> Theme.NIGHT
                else -> Theme.AUTO
            }
            viewModel.updateTheme(theme)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}