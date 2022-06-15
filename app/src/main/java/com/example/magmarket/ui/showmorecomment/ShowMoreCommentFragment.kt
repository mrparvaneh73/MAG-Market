package com.example.magmarket.ui.showmorecomment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.magmarket.R
import com.example.magmarket.data.remote.ResultWrapper
import com.example.magmarket.databinding.FragmentSendCommentBinding
import com.example.magmarket.databinding.FragmentShowMoreCommentBinding
import com.example.magmarket.databinding.FragmentShowmoreBinding
import com.example.magmarket.ui.adapters.ShowMoreCommentAdapter
import com.example.magmarket.ui.showmoreproduct.ShowMoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShowMoreCommentFragment : Fragment(R.layout.fragment_show_more_comment) {
    private var _binding: FragmentShowMoreCommentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ShowMoreCommentViewModel>()
    private val showMoreCommentAdapter = ShowMoreCommentAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentShowMoreCommentBinding.bind(view)
        init()
        navigateToSubmitCommnet()
        back()

    }

    private fun init() = with(binding) {
        showMoreCommentAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        allcommentRecycler.adapter = showMoreCommentAdapter
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    Log.d("showmore", "init: "+viewModel.productId)
                    viewModel.getAllComment(viewModel.productId!!).collect {
                        showMoreCommentAdapter.submitData(it)
                    }
                }
                launch {
                    showMoreCommentAdapter.loadStateFlow.collect {
                        when (it.refresh) {
                            LoadState.Loading -> {
                                allcommentRecycler.isVisible = false
                                stateView.onLoading()
                            }
                            is LoadState.Error -> {
                                stateView.onFail()
                                stateView.clickRequest {
                                    showMoreCommentAdapter.retry()
                                }

                            }
                            is LoadState.NotLoading -> {
                                stateView.onSuccess()
                                allcommentRecycler.isVisible = true
                            }
                        }
                    }
                }
                launch {
                    viewModel.product.collect {
                        when (it) {
                            is ResultWrapper.Loading -> {


                            }
                            is ResultWrapper.Success -> {
                                Glide.with(this@ShowMoreCommentFragment)
                                    .load(it.value.images?.get(0)!!.src)
                                    .into(commenttoolbar.imageProduct)
                                commenttoolbar.productName.text = it.value.name
                                stateView.onSuccess()
                            }
                            is ResultWrapper.Error -> {
                                stateView.onFail()
                                stateView.clickRequest {

                                }
                            }
                        }
                    }
                }

            }
        }

    }



    private fun back() {
        binding.commenttoolbar.imgBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
private fun navigateToSubmitCommnet(){
    binding.submitComment.setOnClickListener {
       findNavController().navigate(ShowMoreCommentFragmentDirections.actionShowMoreCommentToSendCommentFragment(viewModel.productId.toString()))

    }
}

    private fun <T> StateFlow<T>.collectIt(lifecycleOwner: LifecycleOwner, function: (T) -> Unit) {
        lifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
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