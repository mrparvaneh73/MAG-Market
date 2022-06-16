package com.example.magmarket.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.magmarket.R
import com.example.magmarket.data.remote.model.order.LineItemX
import com.example.magmarket.databinding.CartItemBinding
import java.text.NumberFormat
import java.util.*

class CartAdapter :
    ListAdapter<LineItemX, CartAdapter.MyViewHolder>(CartOrdersDiffCall) {
    val nf: NumberFormat = NumberFormat.getInstance(Locale.US)
    private lateinit var onItemClickListener: OnItemClickListener


    fun setOnItemClickListener(listenerOn: OnItemClickListener) {
        onItemClickListener = listenerOn
        notifyDataSetChanged()
    }

    inner class MyViewHolder(
        private val binding: CartItemBinding,
        private val listenerOn: OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun mBind(productItem: LineItemX) = binding.apply {
            Glide.with(root)
                .load(productItem.meta_data[0].value)
                .into(imgProduct)
            tvProductName.text = productItem.name
            if (productItem.subtotal != "") {
                tvTotalprice.text = nf.format(productItem.subtotal.toInt())
            } else {
                tvTotalprice.text = "بدون قیمت"
            }
            if (productItem.quantity == 1) {
                imgDeleteOrder.setImageResource(R.drawable.delete)
            } else {
                imgDeleteOrder.setImageResource(R.drawable.minus)
            }
            Log.d("gheymat", "mBind: "+productItem.meta_data[1].value)
            Log.d("gheymat", "mBind: ")
            if (productItem.meta_data[1].value.toInt()==productItem.subtotal.toInt()){
                unitOff.isVisible=false
                tvOff.text=""
            }else{
                unitOff.isVisible=true
               tvOff.text=nf.format(productItem.meta_data[1].value.toInt().minus(productItem.subtotal.toInt()) * productItem.quantity)
            }
            tvProductCount.text = productItem.quantity.toString()

            imageViewPlus.setOnClickListener {
                listenerOn.onItemPlus(absoluteAdapterPosition,productItem.id,productItem.quantity,productItem.meta_data[0].value,productItem.meta_data[1].value)
            }
            imgDeleteOrder.setOnClickListener {
                listenerOn.onItemMinus(absoluteAdapterPosition,productItem.id,productItem.quantity,productItem.meta_data[0].value,productItem.meta_data[1].value)
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
        fun onItemPlus(position: Int,id: Int, quantity: Int,image:String,regularPrice:String)
        fun onItemMinus(position: Int,id: Int, quantity: Int,image:String,regularPrice:String)
    }

}

object CartOrdersDiffCall : DiffUtil.ItemCallback<LineItemX>() {

    override fun areItemsTheSame(oldItem: LineItemX, newItem: LineItemX): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LineItemX, newItem: LineItemX): Boolean {
        return oldItem == newItem
    }

}