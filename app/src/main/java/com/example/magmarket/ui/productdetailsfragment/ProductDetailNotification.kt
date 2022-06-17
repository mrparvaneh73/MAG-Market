package com.example.magmarket.ui.productdetailsfragment

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.magmarket.R
import com.example.magmarket.data.remote.Resource
import com.example.magmarket.databinding.FragmentProductNotificationBinding
import com.example.magmarket.ui.adapters.CommentAdapter
import com.example.magmarket.ui.adapters.SimilarAdapter
import com.example.magmarket.ui.adapters.SliderAdapter
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

class ProductDetailNotification: Fragment(R.layout.fragment_product_notification) {
    private val nf: NumberFormat = NumberFormat.getInstance(Locale.US)
    private var _binding: FragmentProductNotificationBinding? = null
    private val binding get() = _binding!!
    private val notifViewModel  by activityViewModels<ProductDetailsViewModel>()
    private val sliderAdapter = SliderAdapter()
    private val commentAdapter = CommentAdapter()
    private var count = 1
    private val similarAdapter = SimilarAdapter(clickListener = {
        findNavController().navigate(
            ProductDetailFragmentDirections.actionProductDetailFragmentSelf(
                it.id
            )
        )
    })
    private lateinit var name: String
    private lateinit var price: String
    private lateinit var image: String
    private var regularPrice: String = "0"
    private lateinit var salePrice: String
    private var similar: MutableList<Int> = mutableListOf(0)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding=FragmentProductNotificationBinding.bind(view)


        init()
        collect()
        close()
    }
   private fun collect() = with(binding) {

        notifViewModel.product.collectIt(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    stateView.onLoading()
                    scrollView3.isVisible = false
                    detailCard.isVisible = false
                }
                is Resource.Success -> {

                    sliderAdapter.submitList(it.value.images)
                    salePrice = it.value.sale_price.toString()
                    Log.d("Saleprice", "collect: " + it.value.sale_price)
            if (it.value.images!!.isNotEmpty()){

                        image = it.value.images?.get(0).src
                    }
                    if (!it.value.name.isNullOrBlank()){
                        name = it.value.name
                        tvProductName.text = name
                    }
                    if (!it.value.description.isNullOrBlank()){
                        tvProductDescription.text =
                            HtmlCompat.fromHtml(it.value.description, HtmlCompat.FROM_HTML_MODE_LEGACY)

                    }
                    if (!it.value.price.isNullOrBlank()){
                        price = it.value.price
                        tvTotalprice.text = nf.format(price.toInt())
                        regularPrice = it.value.regular_price.toString()
                        if (it.value.price.toInt() == regularPrice.toInt()) {
                            tvRegularprice.text = ""
                        } else {
                            tvRegularprice.text = nf.format(regularPrice.toInt())
                            tvRegularprice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                        }
                    }


                    it.value.related_ids?.let { it1 -> similar.addAll(it1) }
                    notifViewModel.getSimilarProduct(similar.toString())



                    scrollView3.isVisible = true
                    detailCard.isVisible = true
                    stateView.onSuccess()
                }
                is Resource.Error -> {
                    stateView.onFail()
                    stateView.clickRequest {
//                        notifViewModel .getProduct(bundle.getString("productId",""))
                    }
                }
            }
        }
    }

    private fun init()= with(binding) {
        val bundle=requireArguments()
        notifViewModel .getProduct(bundle.getString("productId",""))
        productSlider.adapter = sliderAdapter
        productSlider.clipToPadding = false
        productSlider.clipChildren = false
        productSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        commentrecyclerView.adapter = commentAdapter

    }
    private fun close() = with(binding) {
        toolbar.closeButton.setOnClickListener {
           findNavController().navigate(ProductDetailNotificationDirections.actionProductDetailNotificationToHomeFragment())
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
}