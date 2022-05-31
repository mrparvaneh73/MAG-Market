package com.example.magmarket.ui.productdetailsfragment


import android.os.Bundle
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.magmarket.R
import com.example.magmarket.data.local.entities.ProductItemLocal
import com.example.magmarket.databinding.FragmentProductDetailBinding
import com.example.magmarket.ui.adapters.SliderAdapter
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
    private var count = 1
    private var isInCart = true
    private lateinit var name: String
    private lateinit var price: String
    private lateinit var image: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductDetailBinding.bind(view)
        init()
        collect()
        close()
        isExist()
        addToCart()
        goToCart()

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
                    image = it.value.images[0].src
                    name = it.value.name
                    tvProductName.text = name
                    tvProductDescription.text =
                        HtmlCompat.fromHtml(it.value.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    price = it.value.price
                    tvTotalprice.text = nf.format(price.toInt())
                    adapter.submitList(it.value.images)
                    scrollView3.isVisible = true
                    detailCard.isVisible = true
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

    private fun addToCart() {
        binding.imageViewPlus.setOnClickListener {

            count++
            viewModel.updateOrder(
                ProductItemLocal(
                    id = args.id.toInt(),
                    count = count,
                    name = name,
                    price = price,
                    images = image
                )
            )
        }
        binding.imgDeleteOrder.setOnClickListener {
            if (count == 1) {
                viewModel.deletProductFromOrders(
                    ProductItemLocal(
                        id = args.id.toInt(),
                        count = count, name = name, price = price, images = image
                    )
                )
                isInCart = false
            } else if (count > 1) {
                count--
                viewModel.updateOrder(
                    ProductItemLocal(
                        id = args.id.toInt(),
                        count = count,
                        name = name,
                        price = price,
                        images = image
                    )
                )
            }
        }
        binding.buttonAddToCart.setOnClickListener {

            viewModel.insertProductInOrders(
                ProductItemLocal(
                    id = args.id.toInt(),
                    count = count,
                    name = name,
                    price = price,
                    images = image
                )
            )
            isInCart = true
        }
    }

   private fun isExist() {

        viewModel.isExistInOrders(args.id.toInt()).observe(viewLifecycleOwner) {
            if (it == true) {
                isInCart = true
                binding.linearLayout7.isVisible = true
                binding.buttonAddToCart.isVisible = false
                setCountorder()
            } else {
                isInCart = false
                binding.linearLayout7.isVisible = false
                binding.buttonAddToCart.isVisible = true
            }

        }


    }
    fun setCountorder(){
        viewModel.getProductFromOrders(args.id.toInt()).observe(viewLifecycleOwner) {
                count = it.count
                binding.tvProductCount.text = it.count.toString()
                if (it.count == 1) {
                    binding.imgDeleteOrder.setImageResource(R.drawable.delete)
                } else {
                    binding.imgDeleteOrder.setImageResource(R.drawable.minus)
                }




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

    private fun goToCart() {
        binding.toolbar.fragmentcart.setOnClickListener {
            findNavController().navigate(ProductDetailFragmentDirections.actionProductDetailFragmentToCartFragment())
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