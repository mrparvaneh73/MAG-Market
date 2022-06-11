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
    private var isLogInUser = false
    private val args by navArgs<ProductDetailFragmentArgs>()
    private val productViewModel by activityViewModels<ProductDetailsViewModel>()
    private val sliderAdapter = SliderAdapter()
    private val commentAdapter = CommentAdapter()
//    private var count = 1


    //    private var productId = 0
//    private var orderSize = 0
    private val similarAdapter = SimilarAdapter(clickListener = {
        findNavController().navigate(
            ProductDetailFragmentDirections.actionProductDetailFragmentSelf(
                it.id
            )
        )
    })

    //    private lateinit var name: String
//    private lateinit var price: String
//    private lateinit var image: String
//    private var regularPrice: String = "0"
//    private lateinit var salePrice: String
    private var similar: MutableList<Int> = mutableListOf(0)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductDetailBinding.bind(view)
        binding.rvSimilarProduct.adapter = similarAdapter
        collect()
        isUserLogin()
        getSimilarProduct()
        init()
        isUserLogin()
        plusOrMinusProduct()
        close()
        goToCart()
        comment()

    }

    private fun init() = with(binding) {
        productViewModel.productId = args.id
        productViewModel.getProduct()
        productSlider.adapter = sliderAdapter
        productSlider.clipToPadding = false
        productSlider.clipChildren = false
        productSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        springDotsIndicator.attachTo(binding.productSlider)
        commentrecyclerView.adapter = commentAdapter

    }

    fun getSimilarProduct() {
        productViewModel.similarProducts.collectIt(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> binding.stateView.onLoading()
                is ResultWrapper.Success -> {
                    similarAdapter.submitList(it.value)
                    if (it.value.isNotEmpty()) {
                        binding.stateView.onSuccess()
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
        productViewModel.product.collectIt(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {
                    stateView.onLoading()
                    scrollView3.isVisible = false
                    detailCard.isVisible = false
                }
                is ResultWrapper.Success -> {
                    stateView.onSuccess()
                    if (!it.value.images.isNullOrEmpty()) {
                        sliderAdapter.submitList(it.value.images)
                    }

//                    salePrice = it.value.sale_price
//                    Log.d("Saleprice", "collect: " + it.value.sale_price)
                    if (!it.value.name.isNullOrEmpty()) {
                        tvProductName.text = it.value.name
                    }

                    tvProductDescription.text =
                        it.value.description?.let { it1 ->
                            HtmlCompat.fromHtml(
                                it1,
                                HtmlCompat.FROM_HTML_MODE_LEGACY
                            )
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

//                    regularPrice = it.value.regular_price


                    scrollView3.isVisible = true
                    detailCard.isVisible = true

                }
                is ResultWrapper.Error -> {
                    stateView.onFail()
                    stateView.clickRequest {
                        productViewModel.getProduct()
                    }
                }
            }
        }
    }


    private fun plusOrMinusProduct() {
        binding.imageViewPlus.setOnClickListener {

            productViewModel.updateAnItemInOrder( productViewModel.count.plus(1))
            responseUpdateOrder()
        }
        binding.imgDeleteOrder.setOnClickListener {

            productViewModel.updateAnItemInOrder(productViewModel.count.minus(1))
            responseUpdateOrder()

        }

    }

    private fun comment() {
        productViewModel.getProductComment(args.id.toInt())
        productViewModel.productComment.collectIt(viewLifecycleOwner) {
            when (it) {

                is ResultWrapper.Success -> {
                    commentAdapter.submitList(it.value)
                }

                else -> {}
            }

        }
    }

    fun responseUpdateOrder() {
        productViewModel.orderUpdate.collectIt(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Success -> {
                    for (i in it.value.line_items) {
                        if (args.id.toInt() == i.product_id) {
                            productViewModel.productId = i.id.toString()
                            binding.tvProductCount.text=i.quantity.toString()
                            productViewModel.count=i.quantity
                            binding.linearLayout7.isVisible = true
                            binding.buttonAddToCart.isVisible = false
                            break
                        }else{

                            binding.linearLayout7.isVisible = false
                            binding.buttonAddToCart.isVisible = true
                        }
                    }

                }

                else -> {}
            }
        }
    }

    private fun responseGetAnOrder() {
        productViewModel.order.collectIt(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Success -> {
                    Log.d("getorder", "responseGetAnOrder: +success"+it.value.line_items.toString())
                    for (i in it.value.line_items) {
                        Log.d("getorder", "responseGetAnOrder: +success"+args.id.toString())
                        if (args.id.toInt() == i.product_id) {

                            productViewModel.productId = i.id.toString()
                            binding.linearLayout7.isVisible = true
                            binding.buttonAddToCart.isVisible = false
                            productViewModel.count=i.quantity
                            binding.tvProductCount.text=i.quantity.toString()
                            break

                        } else {

                            binding.linearLayout7.isVisible = false
                            binding.buttonAddToCart.isVisible = true
                        }
                    }

                }

                is ResultWrapper.Error -> {
                    Log.d("getorder", "responseGetAnOrder: +error")
                }
            }
        }


    }

    private fun collectResponseOrder() {
        productViewModel.orderCreate.collectIt(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {

                }
                is ResultWrapper.Success -> {

                    productViewModel.saveUserDataStore(
                        com.example.magmarket.data.datastore.user.User(
                            userId = productViewModel.customerId,
                            email = productViewModel.customerEmail,
                            firstName = productViewModel.customerFirstName,
                            lastName = productViewModel.customerLastName,
                            orderId = it.value.id,
                            orderStatus = "pending",
                            isLogin = true
                        )
                    )

                    productViewModel.getAnOrder(it.value.id)
                    responseGetAnOrder()

                }
                is ResultWrapper.Error -> {
                    binding.stateView.onFail()

                }
            }
        }
    }

    private fun isUserLogin() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                productViewModel.getUserFromDataStore().collect { user ->
                    binding.buttonAddToCart.setOnClickListener {
                        if (user.isLogin) {
                            productViewModel.setUserInfo(user)

                            if (productViewModel.orderId == 0) {
                                productViewModel.createOrder()
                                collectResponseOrder()
                            } else {
                                productViewModel.addAnItemInOrder(args.id.toInt())
                                responseUpdateOrder()
                                productViewModel.getAnOrder(user.orderId)
                                responseGetAnOrder()
                            }

                        } else {
                            openDialog()

                        }

                    }
                    productViewModel.orderId = user.orderId
                    if (user.orderId != 0) {
                        productViewModel.getAnOrder(user.orderId)
                        responseGetAnOrder()
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