package com.example.magmarket.ui.productdetailsfragment

import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.magmarket.R
import com.example.magmarket.data.remote.ResultWrapper
import com.example.magmarket.data.remote.model.review.Review
import com.example.magmarket.databinding.FragmentSendCommentBinding
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SendCommentFragment : Fragment(R.layout.fragment_send_comment) {
    private var _binding: FragmentSendCommentBinding? = null
    private val binding get() = _binding!!
    var productRate: Boolean = false
    lateinit var userEmail: String

    lateinit var user: String
    private val args by navArgs<SendCommentFragmentArgs>()
    private val sendCommentViewModel by activityViewModels<ProductDetailsViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSendCommentBinding.bind(view)
        init()
        collect()
        loginFromLocal()
        checkingForsendComment()
        back()
        sendComment()
        binding.productRate.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                when (progress) {
                    0 -> {
                        binding.ratingText.isVisible = false
                        binding.btnSubmitReview.isEnabled=false

                    }
                    1 -> {
                        binding.ratingText.isVisible = true
                        binding.ratingText.text = "ضعیف"
                        binding.btnSubmitReview.isEnabled=true

                    }
                    2 -> {
                        binding.ratingText.isVisible = true
                        binding.ratingText.text = "معمولی"
                        binding.btnSubmitReview.isEnabled=true

                    }
                    3 -> {
                        binding.ratingText.isVisible = true
                        binding.ratingText.text = "خوب"
                        binding.btnSubmitReview.isEnabled=true

                    }
                    4 -> {
                        binding.ratingText.isVisible = true
                        binding.ratingText.text = "بسیارخوب"
                        binding.btnSubmitReview.isEnabled=true

                    }
                    5 -> {
                        binding.ratingText.isVisible = true
                        binding.ratingText.text = "فوق العاده"
                        binding.btnSubmitReview.isEnabled=true
                    }

                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }

    private fun collect() = with(binding) {
        sendCommentViewModel.product.collectIt(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {
                    stateView.onLoading()
                    parentComment.isVisible = false

                }
                is ResultWrapper.Success -> {
                    Glide.with(this@SendCommentFragment).load(it.value.images[0].src)
                        .into(commenttoolbar.imageProduct)
                    commenttoolbar.productName.text = it.value.name

                    parentComment.isVisible = true
                    stateView.onSuccess()
                }
                is ResultWrapper.Error -> {
                    stateView.onFail()
                    stateView.clickRequest {
                        sendCommentViewModel.getProduct(args.id)
                    }
                }
            }
        }
    }

    private fun init() {
        binding.btnSubmitReview.isEnabled=false
        sendCommentViewModel.getProduct(args.id)
    }

    private fun loginFromLocal() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sendCommentViewModel.getUserFromLocal().collect {
                    if (it.isNotEmpty()) {
                        user = it[it.lastIndex].firstName + it[it.lastIndex].lastName
                        userEmail = it[it.lastIndex].email
                    }

                }
            }
        }

    }

    private fun sendComment() {
        binding.btnSubmitReview.setOnClickListener {
            Log.d("infocomment", "sendComment: ${id}")
            sendCommentViewModel.sendUserComment(
                Review(
                    product_id = args.id.toInt(),
                    rating = binding.productRate.progress,
                    review = binding.edUserComment.text.toString(),
                    reviewer = user,
                    reviewer_email = userEmail
                )
            )
        }
        sendCommentViewModel.responseComment.collectIt(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Success -> {
                    Toast.makeText(requireContext(), "با موفیقیت ارسال شد", Toast.LENGTH_SHORT)
                        .show()

                }
                is ResultWrapper.Error -> {

                    Toast.makeText(requireContext(), "پیام شما ارسال نشد", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    }

    private fun checkingForsendComment() = with(binding) {

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
        binding.commenttoolbar.backImg.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}