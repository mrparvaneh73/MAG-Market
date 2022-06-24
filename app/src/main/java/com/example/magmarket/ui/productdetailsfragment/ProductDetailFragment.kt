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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.magmarket.R
import com.example.magmarket.data.remote.Resource
import com.example.magmarket.databinding.FragmentProductDetailBinding
import com.example.magmarket.ui.adapters.CommentAdapter
import com.example.magmarket.ui.adapters.SimilarAdapter
import com.example.magmarket.ui.adapters.SliderAdapter
import com.google.android.material.button.MaterialButton

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*


@AndroidEntryPoint
class ProductDetailFragment : Fragment(R.layout.fragment_product_detail) {
    private val nf: NumberFormat = NumberFormat.getInstance(Locale.US)
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!


    private val productViewModel by viewModels<ProductDetailsViewModel>()
    private val sliderAdapter = SliderAdapter()
    private val commentAdapter = CommentAdapter()
    private val similarAdapter = SimilarAdapter(clickListener = { it ->
        findNavController().navigate(
            ProductDetailFragmentDirections.actionProductDetailFragmentSelf(
                it.id
            )
        )
    })

    private var similar: MutableList<Int> = mutableListOf(0)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductDetailBinding.bind(view)
        binding.rvSimilarProduct.adapter = similarAdapter


        collect()
        isUserLogin()
        getSimilarProduct()
        init()
        plusOrMinusProduct()
        close()
        goToCart()
        comment()
        responseUpdateOrder()
        responseGetAnOrder()
        showMoreComment()
        collectResponseOrder()
    }

    private fun init() = with(binding) {

        productSlider.adapter = sliderAdapter
        productSlider.clipToPadding = false
        productSlider.clipChildren = false
        productSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        springDotsIndicator.attachTo(binding.productSlider)
        commentrecyclerView.adapter = commentAdapter


    }

    private fun getSimilarProduct() {
        productViewModel.similarProducts.collectIt(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> binding.stateView.onLoading()
                is Resource.Success -> {
                    similarAdapter.submitList(it.value)
                    if (it.value.isNotEmpty()) {
                        binding.stateView.onSuccess()
                    } else {
                        binding.stateView.onEmpty()
                    }
                }
                is Resource.Error -> {

                }
            }
        }
    }

    fun collect() = with(binding) {
        productViewModel.product.collectIt(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    stateView.onLoading()
                    scrollView3.isVisible = false
                    detailCard.isVisible = false
                }
                is Resource.Success -> {
                    stateView.onSuccess()
                    if (!it.value.images.isNullOrEmpty()) {
                        sliderAdapter.submitList(it.value.images)
                    }

                    if (!it.value.name.isNullOrEmpty()) {
                        tvProductName.text = it.value.name
                    }
                    if (!it.value.description.isNullOrEmpty()) {
                        tvProductDescription.text =
                            it.value.description.let { it1 ->
                                HtmlCompat.fromHtml(
                                    it1,
                                    HtmlCompat.FROM_HTML_MODE_LEGACY
                                )
                            }

                    }else{
                        tvProductDescription.text =""
                    }


                    it.value.related_ids?.let { it1 -> similar.addAll(it1) }
                    productViewModel.getSimilarProduct(similar.toString())
                    if (!it.value.price.isNullOrEmpty()) {
                        tvTotalprice.text = nf.format(it.value.price.toInt())
                        if (it.value.price.toInt() == it.value.regular_price!!.toInt()) {
                            tvRegularprice.text = ""
                        } else {
                            tvRegularprice.text = nf.format(it.value.regular_price.toInt())
                            tvRegularprice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                        }
                    } else {
                        tvTotalprice.text = "محصول فاقد قیمت"
                    }
                    productViewModel.productImage = it.value.images?.get(0)?.src ?: ""
                    productViewModel.regular_price = it.value.regular_price ?: ""
//                    regularPrice = it.value.regular_price


                    scrollView3.isVisible = true
                    detailCard.isVisible = true

                }
                is Resource.Error -> {
                    stateView.onFail()
                    stateView.clickRequest {
//                       productViewModel.getProduct()
                    }
                }
            }
        }
    }


    private fun plusOrMinusProduct() {
        binding.imageViewPlus.setOnClickListener {
            productViewModel.updateAnItemInOrder(productViewModel.count.plus(1))
            productViewModel.count++
        }

        binding.imgDeleteOrder.setOnClickListener {
            productViewModel.updateAnItemInOrder(productViewModel.count.minus(1))
            productViewModel.count--
        }
    }

    private fun comment() {
        productViewModel.getProductComment()
        productViewModel.productComment.collectIt(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    commentAdapter.submitList(it.value)
                }

                else -> {}
            }

        }
    }

    fun responseUpdateOrder() = with(binding) {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                productViewModel.orderUpdate.collectLatest {
                    when (it) {
                        is Resource.Loading -> {
                            loadingCount.isVisible = true
                            tvProductCount.isVisible = false
                            parentPlusandminus.isClickable = false
                            loadingCount.playAnimation()
                        }
                        is Resource.Success -> {

                            parentPlusandminus.isClickable = true
                            if (it.value.line_items.isNotEmpty()) {
                                for (i in it.value.line_items) {
                                    if (productViewModel.productId!!.toInt() == i.product_id) {
                                        productViewModel.id = i.id

                                        productViewModel.count = i.quantity
                                        tvProductCount.text = productViewModel.count.toString()
                                        parentPlusandminus.isVisible = true
                                        buttonAddToCart.isVisible = false
                                        loadingCount.isVisible = false
                                        tvProductCount.isVisible = true
                                        loadingCount.pauseAnimation()
                                        break
                                    } else {
                                        parentPlusandminus.isVisible = false
                                        buttonAddToCart.isVisible = true
                                    }
                                }
                            } else {
                                parentPlusandminus.isVisible = false
                                buttonAddToCart.isVisible = true
                            }


                        }

                        is Resource.Error -> {
                            loadingCount.isVisible = false
                            loadingCount.pauseAnimation()
                        }
                    }
                }
            }
        }
    }


    private fun responseGetAnOrder() = with(binding) {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                productViewModel.order.collectLatest {
                    when (it) {
                        is Resource.Loading -> {
                            loadingCount.isVisible = true
                            loadingCount.playAnimation()
                            buttonAddToCart.isVisible = false
                            parentPlusandminus.isVisible = false
                            binding.buttonAddToCart.isVisible = false
                        }
                        is Resource.Success -> {
                            if (it.value.line_items.isNotEmpty()) {
                                for (i in it.value.line_items) {

                                    if (productViewModel.productId!!.toInt() == i.product_id) {
                                        productViewModel.id = i.id
                                        loadingCount.isVisible = false
                                        parentPlusandminus.isVisible = true
                                        binding.buttonAddToCart.isVisible = false
                                        productViewModel.count = i.quantity
                                        binding.tvProductCount.text =
                                            productViewModel.count.toString()
                                        break

                                    } else {

                                        parentPlusandminus.isVisible = false
                                        buttonAddToCart.isVisible = true
                                    }
                                }
                            } else {
                                parentPlusandminus.isVisible = false
                                buttonAddToCart.isVisible = true
                            }


                        }

                        is Resource.Error -> {
                            loadingCount.isVisible = false
                            loadingCount.pauseAnimation()

                        }
                    }
                }
            }
        }


    }

    private fun collectResponseOrder() = with(binding) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                productViewModel.orderCreate.collect {
                    when (it) {
                        is Resource.Loading -> {
                            loadingAdd.isVisible = true
                            loadingCount.playAnimation()
                        }
                        is Resource.Success -> {

                            productViewModel.saveUserDataStore(
                                com.example.magmarket.data.datastore.user.User(
                                    userId = productViewModel.customerId,
                                    email = productViewModel.customerEmail,
                                    firstName = productViewModel.customerFirstName,
                                    lastName = productViewModel.customerLastName,
                                    myorderId = it.value.id,
                                    orderStatus = "pending",
                                    isLogin = true
                                )
                            )
                            loadingAdd.isVisible = false
                            loadingCount.pauseAnimation()

                            productViewModel.getAnOrder(it.value.id)


                        }
                        is Resource.Error -> {
                            binding.stateView.onFail()

                        }
                    }
                }
            }
        }

    }

    private fun isUserLogin() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                productViewModel.getUserFromDataStore().collect { user ->
                    binding.buttonAddToCart.setOnClickListener {
                        Log.d("isuserlogin", "isUserLogin: " + user.isLogin)
                        if (user.isLogin) {
                            productViewModel.setUserInfo(user)

                            if (productViewModel.orderId == 0) {
                                productViewModel.createOrder()
                            } else {
                                productViewModel.addAnItemInOrder()
                            }

                        } else {
                            openDialog()

                        }

                    }
                    binding.submitComment.setOnClickListener {
                        if (user.isLogin) {
                            findNavController().navigate(
                                ProductDetailFragmentDirections.actionProductDetailFragmentToSendCommentFragment(
                                    productViewModel.productId!!
                                )
                            )
                        } else {
                            openDialog()
                        }

                    }
                    productViewModel.orderId = user.myorderId
                    if (user.myorderId != 0) {
                        productViewModel.getAnOrder(user.myorderId)
                    }
                }
            }
        }
    }


    private fun goToCart() {
        binding.toolbar.fragmentcart.setOnClickListener {
            findNavController().navigate(
                ProductDetailFragmentDirections.actionProductDetailFragmentToParentOfCartFragment(

                )
            )


        }

    }


    private fun close() = with(binding) {
        toolbar.closeButton.setOnClickListener {
            requireActivity().onBackPressed()
        }


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

    private fun showMoreComment() {
        binding.showAllComment.setOnClickListener {
            findNavController().navigate(
                ProductDetailFragmentDirections.actionProductDetailFragmentToShowMoreComment(
                    productViewModel.productId!!.toInt()
                )
            )
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