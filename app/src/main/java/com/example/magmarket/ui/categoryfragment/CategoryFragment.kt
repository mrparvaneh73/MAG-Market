package com.example.magmarket.ui.categoryfragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.magmarket.R
import com.example.magmarket.adapters.SubCategoryAdapter
import com.example.magmarket.data.model.ProductRecyclerViewItem
import com.example.magmarket.databinding.FragmentCategoryBinding
import com.example.magmarket.databinding.FragmentHomeBinding
import com.example.magmarket.utils.ResultWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment:Fragment(R.layout.fragment_category) {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<CategoryViewModel>()
    val artAdapter= SubCategoryAdapter()
    val supermarketAdapter=SubCategoryAdapter()
    val digitalAdapter=SubCategoryAdapter()
    val fashionAdapter=SubCategoryAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCategoryBinding.bind(view)
        init()
        collector()

    }


    fun init() {
        binding.artRecyclerview.adapter=artAdapter
        binding.digitalRecyclerview.adapter=digitalAdapter
        binding.supermarketRecyclerview.adapter=supermarketAdapter
        binding.fashionRecyclerview.adapter=fashionAdapter
    }
fun collector(){
    viewModel.digitalCategory.collectIt(viewLifecycleOwner){
        when (it) {
            is ResultWrapper.Loading -> {
                binding.mainviewgroup.isVisible=false
                binding.stateView.onLoading()
            }
            is ResultWrapper.Success -> {
                binding.mainviewgroup.isVisible=true
                digitalAdapter.submitList(it.value)
                if (it.value.isNotEmpty()) {
                    binding.stateView.onSuccess()
                } else {
                    binding.stateView.onEmpty()
                }
            }
            is ResultWrapper.Error -> {
                binding.stateView.onFail()
                binding.stateView.clickRequest {
                    viewModel.getSubCategories()
                }
            }
        }
    }
    viewModel.fashionCategory.collectIt(viewLifecycleOwner){
        when (it) {
            is ResultWrapper.Loading -> {
                binding.mainviewgroup.isVisible=false
                binding.stateView.onLoading()
            }
            is ResultWrapper.Success -> {
                binding.mainviewgroup.isVisible=true
                fashionAdapter.submitList(it.value)
                if (it.value.isNotEmpty()) {
                    binding.stateView.onSuccess()
                } else {
                    binding.stateView.onEmpty()
                }
            }
            is ResultWrapper.Error -> {
                binding.stateView.onFail()
                binding.stateView.clickRequest {
                    viewModel.getSubCategories()
                }
            }
        }
    }
    viewModel.superMarketCategory.collectIt(viewLifecycleOwner){
        when (it) {
            is ResultWrapper.Loading -> {
                binding.mainviewgroup.isVisible=false
                binding.stateView.onLoading()
            }
            is ResultWrapper.Success -> {
                binding.mainviewgroup.isVisible=true
                supermarketAdapter.submitList(it.value)
                if (it.value.isNotEmpty()) {
                    binding.stateView.onSuccess()
                } else {
                    binding.stateView.onEmpty()
                }
            }
            is ResultWrapper.Error -> {
                binding.stateView.onFail()
                binding.stateView.clickRequest {
                    viewModel.getSubCategories()
                }
            }
        }
    }
    viewModel.artCategory.collectIt(viewLifecycleOwner){
        when (it) {
            is ResultWrapper.Loading -> {
                binding.mainviewgroup.isVisible=false
                binding.stateView.onLoading()
            }
            is ResultWrapper.Success -> {
                binding.mainviewgroup.isVisible=true
                artAdapter.submitList(it.value)
                if (it.value.isNotEmpty()) {
                    binding.stateView.onSuccess()
                } else {
                    binding.stateView.onEmpty()
                }
            }
            is ResultWrapper.Error -> {
                binding.stateView.onFail()
                binding.stateView.clickRequest {
                    viewModel.getSubCategories()
                }
            }
        }
    }
}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
    fun <T> StateFlow<T>.collectIt(lifecycleOwner: LifecycleOwner, function: (T) -> Unit) {
        lifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collect {
                    function.invoke(it)
                }
            }
        }
    }
}