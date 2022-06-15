package com.example.magmarket.ui.sendcommentfragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.magmarket.R
import com.example.magmarket.data.remote.Resource
import com.example.magmarket.data.remote.model.review.Review
import com.example.magmarket.databinding.FragmentSendCommentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
@AndroidEntryPoint
class SendCommentFragment : Fragment(R.layout.fragment_send_comment) {
    private var _binding: FragmentSendCommentBinding? = null
    private val binding get() = _binding!!
    private val sendCommentViewModel by viewModels<SendCommentViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSendCommentBinding.bind(view)
        init()
        collect()
        rating()
        checkingForSendComment()
        loginFromLocal()
        back()

    }

    private fun collect() = with(binding) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    sendCommentViewModel.product.collect {
                        when (it) {
                            is Resource.Loading -> {
                                stateView.onLoading()
                                parentComment.isVisible = false

                            }
                            is Resource.Success -> {
                                Glide.with(this@SendCommentFragment).load(it.value.images?.get(0)!!.src)
                                    .into(commenttoolbar.imageProduct)
                                commenttoolbar.productName.text = it.value.name

                                parentComment.isVisible = true
                                stateView.onSuccess()
                            }
                            is Resource.Error -> {
                                stateView.onFail()
                                stateView.clickRequest {

                                }
                            }
                        }
                    }
                }
                launch {
                    sendCommentViewModel.responseComment.collect {
                        when (it) {
                            is Resource.Success -> {
                                Toast.makeText(requireContext(), "با موفیقیت ارسال شد", Toast.LENGTH_SHORT)
                                    .show()

                            }
                            is Resource.Error -> {

                                Toast.makeText(requireContext(), "پیام شما ارسال نشد", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            }
        }

    }

    private fun rating() = with(binding) {
        productRate.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                when (progress) {
                    0 -> {
                        binding.ratingText.isVisible = false
                        binding.btnSubmitReview.isEnabled = false

                    }
                    1 -> {
                        ratingText.isVisible = true
                        ratingText.text = "ضعیف"
                        btnSubmitReview.isEnabled = true

                    }
                    2 -> {
                        ratingText.isVisible = true
                        ratingText.text = "معمولی"
                        btnSubmitReview.isEnabled = true

                    }
                    3 -> {
                        ratingText.isVisible = true
                        ratingText.text = "خوب"
                        btnSubmitReview.isEnabled = true

                    }
                    4 -> {
                        ratingText.isVisible = true
                        ratingText.text = "بسیارخوب"
                        btnSubmitReview.isEnabled = true

                    }
                    5 -> {
                        ratingText.isVisible = true
                        ratingText.text = "فوق العاده"
                        btnSubmitReview.isEnabled = true
                    }

                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }

    private fun init() {
        binding.btnSubmitReview.isEnabled = false

    }

    private fun loginFromLocal() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sendCommentViewModel.getUserFromDataStore().collect { user ->
                    if (user.isLogin) {
                        binding.btnSubmitReview.setOnClickListener {
                            Log.d("infocomment", "sendComment: ${id}" +sendCommentViewModel.sendCommentProductId)
                            sendCommentViewModel.sendUserComment(
                                Review(
                                    product_id = sendCommentViewModel.sendCommentProductId!!.toInt(),
                                    rating = binding.productRate.progress,
                                    review = binding.edUserComment.text.toString(),
                                    reviewer = user.firstName + user.lastName,
                                    reviewer_email = user.email
                                )
                            )
                        }

                    }

                }
            }
        }

    }


    private fun checkingForSendComment() = with(binding) {

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnSubmitReview.isEnabled =
                    binding.edUserComment.text.toString().trim().isNotEmpty()

            }

            override fun afterTextChanged(s: Editable?) {

            }

        }
        binding.edUserComment.addTextChangedListener(textWatcher)

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

    private fun back() {
        binding.commenttoolbar.imgBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}