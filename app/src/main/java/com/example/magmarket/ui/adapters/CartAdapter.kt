package com.example.magmarket.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.magmarket.R
import com.example.magmarket.data.local.entities.ProductItemLocal
import com.example.magmarket.databinding.CartItemBinding
import java.text.NumberFormat
import java.util.*

class CartAdapter() :
    ListAdapter<ProductItemLocal, CartAdapter.MyViewHolder>(CartOrdersDiffCall) {
    val nf: NumberFormat = NumberFormat.getInstance(Locale.US)
    private lateinit var onItemClickListener: CartAdapter.OnItemClickListener


    fun setOnItemClickListener(listenerOn: CartAdapter.OnItemClickListener) {
        onItemClickListener = listenerOn
    }

    inner class MyViewHolder(
        private val binding: CartItemBinding,
        private val listenerOn: CartAdapter.OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun mBind(productItem: ProductItemLocal) = binding.apply {
            Glide.with(root)
                .load(productItem.images)
                .into(imgProduct)
            tvProductName.text = productItem.name
            if (productItem.price != "") {
                tvPrice.text = nf.format(productItem.price?.toInt())
            } else {
                tvPrice.text = "بدون قیمت"
            }
            if (productItem.count == 1) {
                imgDeleteOrder.setImageResource(R.drawable.delete)
            } else {
                imgDeleteOrder.setImageResource(R.drawable.minus)
            }
            tvProductCount.text = productItem.count.toString()

            imageViewPlus.setOnClickListener {
                listenerOn.onItemPlus(absoluteAdapterPosition)
            }
            imgDeleteOrder.setOnClickListener {
                listenerOn.onItemMinus(absoluteAdapterPosition)
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

object CartOrdersDiffCall : DiffUtil.ItemCallback<ProductItemLocal>() {

    override fun areItemsTheSame(oldItem: ProductItemLocal, newItem: ProductItemLocal): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductItemLocal, newItem: ProductItemLocal): Boolean {
        return oldItem == newItem
    }

}