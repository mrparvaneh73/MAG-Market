package com.example.magmarket.ui.productdetailsfragment


import android.app.Dialog
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.magmarket.R
import com.example.magmarket.data.local.entities.ProductItemLocal
import com.example.magmarket.data.remote.ResultWrapper
import com.example.magmarket.databinding.FragmentProductDetailBinding
import com.example.magmarket.ui.adapters.CommentAdapter
import com.example.magmarket.ui.adapters.SimilarAdapter
import com.example.magmarket.ui.adapters.SliderAdapter
import com.google.android.material.button.MaterialButton

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
    private val productDetailsViewModel  by activityViewModels<ProductDetailsViewModel>()
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
        _binding = FragmentProductDetailBinding.bind(view)
        binding.rvSimilarProduct.adapter = similarAdapter
        init()

        close()
        isExist()
        addToCart()
        goToCart()
        getSimilarProduct()
        collect()
        comment()
        loginFromLocal()
        navigateTosubmitComment()

    }

    fun getSimilarProduct()= with(binding) {
    productDetailsViewModel.similarProducts.collectIt(viewLifecycleOwner){
        when (it) {
            is ResultWrapper.Loading -> {
                stateView.onLoading()
                scrollView3.isVisible = false
                detailCard.isVisible = false
            }
            is ResultWrapper.Success -> {

                if (it.value.isNotEmpty()) {
                    scrollView3.isVisible = true
                    detailCard.isVisible = true
                    binding.stateView.onSuccess()
                    similarAdapter.submitList(it.value)
                } else {
                    binding.stateView.onEmpty()
                }
            }
            is ResultWrapper.Error -> {

            }
        }
    }
    }

    fun collect() = with(binding) {

        productDetailsViewModel.product.collectIt(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {
                    stateView.onLoading()
                    scrollView3.isVisible = false
                    detailCard.isVisible = false
                }
                is ResultWrapper.Success -> {

                   sliderAdapter.submitList(it.value.images)
                    salePrice = it.value.sale_price
                    Log.d("Saleprice", "collect: " + it.value.sale_price)
                    if (it.value.images.isNotEmpty()){

                        image = it.value.images[0].src
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
                        regularPrice = it.value.regular_price
                        if (it.value.price.toInt() == regularPrice.toInt()) {
                            tvRegularprice.text = ""
                        } else {
                            tvRegularprice.text = nf.format(regularPrice.toInt())
                            tvRegularprice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                        }
                    }


                    similar.addAll(it.value.related_ids)
                    productDetailsViewModel.getSimilarProduct(similar.toString())



                    scrollView3.isVisible = true
                    detailCard.isVisible = true
                    stateView.onSuccess()
                }
                is ResultWrapper.Error -> {
                    stateView.onFail()
                    stateView.clickRequest {
                        productDetailsViewModel.getProduct(args.id)
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

    private fun addToCart() {
        binding.imageViewPlus.setOnClickListener {
            count++
            binding.tvProductCount.text = count.toString()
            productDetailsViewModel.updateOrder(
                ProductItemLocal(
                    id = args.id.toInt(),
                    count = count,
                    name = name,
                    price = price,
                    images = image,
                    regular_price = regularPrice,
                    sale_price = salePrice
                )
            )

        }
        binding.imgDeleteOrder.setOnClickListener {
            if (count == 1) {

                productDetailsViewModel.deletProductFromOrders(
                    ProductItemLocal(
                        id = args.id.toInt(),
                        count = count, name = name, price = price, images = image,
                        regular_price = regularPrice
                    )
                )

            } else if (count > 1) {
                count--
                binding.tvProductCount.text = count.toString()
                productDetailsViewModel.updateOrder(
                    ProductItemLocal(
                        id = args.id.toInt(),
                        count = count,
                        name = name,
                        price = price,
                        images = image,
                        regular_price = regularPrice
                    )
                )
            }
        }
        binding.buttonAddToCart.setOnClickListener {

            productDetailsViewModel.insertProductInOrders(
                ProductItemLocal(
                    id = args.id.toInt(),
                    count = count,
                    name = name,
                    price = price,
                    images = image,
                    regular_price = regularPrice,
                    sale_price = salePrice
                )
            )

        }
    }

    private fun isExist() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                productDetailsViewModel.isExistInOrders(args.id.toInt()).collect {
                    if (it) {

                        setCountOrder()

                        binding.linearLayout7.isVisible = true
                        binding.buttonAddToCart.isVisible = false

                    } else {
                        binding.linearLayout7.isVisible = false
                        binding.buttonAddToCart.isVisible = true
                    }

                }
            }
        }


    }

    private fun setCountOrder() {
        binding.tvProductCount.text = count.toString()
        if (count == 1) {
            binding.imgDeleteOrder.setImageResource(R.drawable.delete)
        } else {
            binding.imgDeleteOrder.setImageResource(R.drawable.minus)
        }
    }

    private fun init()= with(binding) {
        productDetailsViewModel.getProduct(args.id)
        productSlider.adapter = sliderAdapter
        productSlider.clipToPadding = false
        productSlider.clipChildren = false
        productSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        commentrecyclerView.adapter = commentAdapter

    }

    private fun goToCart() {
        binding.toolbar.fragmentcart.setOnClickListener {
            findNavController().navigate(
                ProductDetailFragmentDirections.actionProductDetailFragmentToParentOfCartFragment(

                )
            )


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
    private fun comment() {
        productDetailsViewModel.getProductComment(args.id.toInt())
        productDetailsViewModel.productComment.collectIt(viewLifecycleOwner) {
            when (it) {

                is ResultWrapper.Success -> {
                    commentAdapter.submitList(it.value)
                }

                else -> {}
            }

        }
    }

    private fun loginFromLocal() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                productDetailsViewModel.getUserFromLocal().collect {
                    productDetailsViewModel.isUserLogin = it.isNotEmpty()

                }
            }
        }

    }

    private fun navigateTosubmitComment(){

        binding.submitComment.setOnClickListener {
            if (productDetailsViewModel.isUserLogin==true){
                findNavController().navigate(ProductDetailFragmentDirections.actionProductDetailFragmentToSendCommentFragment(args.id))
            }else{
                openDialog()
            }

        }

    }

    fun getProductFromNotification(){

    }

    private fun openDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.first_login)
        val button = dialog.findViewById<ImageView>(R.id.img_close)
        val button_login = dialog.findViewById<MaterialButton>(R.id.btn_login)

        button_login.setOnClickListener {
            findNavController().navigate(ProductDetailFragmentDirections.actionGlobalUserFragment())
            dialog.dismiss()
        }
        button.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}