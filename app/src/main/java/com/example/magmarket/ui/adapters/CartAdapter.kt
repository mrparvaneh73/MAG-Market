package com.example.magmarket.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.magmarket.R
import com.example.magmarket.data.local.entities.ProductItemLocal
import com.example.magmarket.data.remote.model.Cart
import com.example.magmarket.data.remote.model.ProductItem
import com.example.magmarket.databinding.CartItemBinding
import java.text.NumberFormat
import java.util.*

class CartAdapter :
    ListAdapter<Cart, CartAdapter.MyViewHolder>(CartOrdersDiffCall) {
    val nf: NumberFormat = NumberFormat.getInstance(Locale.US)
    private lateinit var onItemClickListener: OnItemClickListener


    fun setOnItemClickListener(listenerOn: OnItemClickListener) {
        onItemClickListener = listenerOn
    }

    inner class MyViewHolder(
        private val binding: CartItemBinding,
        private val listenerOn: OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun mBind(productItem: Cart) = binding.apply {
            Glide.with(root)
                .load(productItem.images)
                .into(imgProduct)
            tvProductName.text = productItem.name
            if (productItem.price != "") {
                tvTotalprice.text = nf.format(productItem.price?.toInt())
            } else {
                tvTotalprice.text = "بدون قیمت"
            }
            if (productItem.count == 1) {
                imgDeleteOrder.setImageResource(R.drawable.delete)
            } else {
                imgDeleteOrder.setImageResource(R.drawable.minus)
            }
            if (productItem.sale_price!=productItem.price){
                unitOff.isVisible=false
                tvOff.text=""
            }else{
                unitOff.isVisible=true
               tvOff.text=nf.format(productItem.off)
            }
            tvProductCount.text = productItem.count.toString()

            imageViewPlus.setOnClickListener {
                listenerOn.onItemPlus(productItem.productId)
            }
            imgDeleteOrder.setOnClickListener {
                listenerOn.onItemMinus(productItem.productId)
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): CartAdapter.MyViewHolder {
        return MyViewHolder(
            CartItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onItemClickListener
        )
    }


    override fun onBindViewHolder(
        holder: CartAdapter.MyViewHolder, position: Int
    ) {
        holder.mBind(getItem(position))
    }

    interface OnItemClickListener {
        fun onItemPlus(position: Int)
        fun onItemMinus(position: Int)
    }

}

object CartOrdersDiffCall : DiffUtil.ItemCallback<Cart>() {

    override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
        return oldItem == newItem
    }

}