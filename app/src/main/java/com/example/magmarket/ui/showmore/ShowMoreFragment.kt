package com.example.magmarket.ui.showmore

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.magmarket.R
import com.example.magmarket.data.remote.ResultWrapper
import com.example.magmarket.ui.adapters.ShowMoreAdapter
import com.example.magmarket.databinding.FragmentShowmoreBinding

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShowMoreFragment : Fragment(R.layout.fragment_showmore) {
    private var _binding: FragmentShowmoreBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ShowMoreViewModel>()

    private val args by navArgs<ShowMoreFragmentArgs>()

    private val showMoreAdapter = ShowMoreAdapter(clickListener = { productItem ->
        findNavController().navigate(
            ShowMoreFragmentDirections.actionShowMoreFragmentToProductDetailFragment(
                productItem.id
            )
        )
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentShowmoreBinding.bind(view)

        backPressed()
//        collect()
        init()
    }

    private fun init() = with(binding) {
        tvOrderBy.text = args.ordername
        showMoreAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        productRecyclerviw.adapter = showMoreAdapter
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.getShowmore(args.orderby).collect {
                        showMoreAdapter.submitData(it)
                    }
                }
                launch {
                    showMoreAdapter.loadStateFlow.collect {
                        when (it.refresh) {
                            LoadState.Loading -> {
                                scrollview.isVisible = false
                                stateView.onLoading()
                            }
                            is LoadState.Error -> {
                                stateView.onFail()
                                stateView.clickRequest {
                                   showMoreAdapter.retry()
                                }

                            }
                            is LoadState.NotLoading ->{
                                stateView.onSuccess()
                                scrollview.isVisible = true
                            }
                        }
                    }
                }

            }
        }

    }

    private fun backPressed() {
        binding.imgBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

//    private fun collect() = with(binding) {
//        viewModel.showmore.collectIt(viewLifecycleOwner) {
//            when (it) {
//                is ResultWrapper.Loading -> {
//                    scrollview.isVisible = false
//                    stateView.onLoading()
//                }
//                is ResultWrapper.Success -> {
//                    scrollview.isVisible = true
//                    showMoreAdapter.submitList(it.value)
//
//                    if (it.value.isNotEmpty()) {
//                        stateView.onSuccess()
//                    } else {
//                        stateView.onEmpty()
//                    }
//                }
//                is ResultWrapper.Error -> {
//                    stateView.onFail()
//                    stateView.clickRequest {
//                        viewModel.getShowmore(args.orderby)
//                    }
//
//                }
//            }
//        }
//
//    }

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