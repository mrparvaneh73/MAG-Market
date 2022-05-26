package com.example.magmarket.ui.productdetailsfragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.magmarket.R
import com.example.magmarket.adapters.SliderAdapter
import com.example.magmarket.databinding.FragmentProductDetailBinding
import com.example.magmarket.utils.ResultWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*


@AndroidEntryPoint
class ProductDetailFragment : Fragment(R.layout.fragment_product_detail) {
    private val nf: NumberFormat = NumberFormat.getInstance(Locale.US)
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ProductDetailFragmentArgs>()
    private val viewModel: ProductDetailsViewModel by viewModels()
    private val adapter = SliderAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductDetailBinding.bind(view)
        init()
        collect()
        close()

    }

    fun collect() = with(binding) {
        viewModel.product.collectIt(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {
                    stateView.onLoading()
                    scrollView3.isVisible = false
                    detailCard.isVisible = false
                }
                is ResultWrapper.Success -> {
                    tvProductName.text = it.value.name
                    tvProductDescription.text =
                        HtmlCompat.fromHtml(it.value.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    tvPrice.text = nf.format(it.value.price.toInt())
                    adapter.submitList(it.value.images)
                    scrollView3.isVisible = true
                    detailCard.isVisible = true
                    stateView.onSuccess()
                    stateView.onSuccess()
                }
                is ResultWrapper.Error -> {
                    stateView.onFail()
                    stateView.clickRequest {
                        viewModel.getProduct(args.id)
                    }
                }
            }
        }
    }

    private fun close() = with(binding) {
        toolbar.closeButton.setOnClickListener {
            requireActivity().onBackPressed()
        }


    }

    private fun init() {
        viewModel.getProduct(args.id)
        binding.productSlider.adapter = adapter
        binding.productSlider.clipToPadding = false
        binding.productSlider.clipChildren = false
        binding.productSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        binding.springDotsIndicator.attachTo(binding.productSlider)
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