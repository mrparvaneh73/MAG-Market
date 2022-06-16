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
import com.example.magmarket.databinding.PlacedOrderPorductItemBinding
import java.text.NumberFormat
import java.util.*

class PlacedOrderDetailsAdapter :
    ListAdapter<LineItemX, PlacedOrderDetailsAdapter.MyViewHolder>(PlacedOrderDetailsAdapterDiffCall) {


    val nf: NumberFormat = NumberFormat.getInstance(Locale.US)
    inner class MyViewHolder(
        private val binding: PlacedOrderPorductItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun mBind(productItem: LineItemX) = binding.apply {
            Glide.with(root)
                .load(productItem.meta_data[0].value)
                .into(imgProduct)
            tvnameproduct.text = productItem.name
            if (productItem.subtotal != "") {
                tvpriceproduct.text = nf.format(productItem.subtotal.toInt())
            } else {
                tvpriceproduct.text = "بدون قیمت"
            }

            if (productItem.meta_data[1].value.toInt()==productItem.subtotal.toInt()){

                regularprice.text=""
            }else{

                regularprice.text=nf.format(productItem.meta_data[1].value.toInt().minus(productItem.subtotal.toInt()) * productItem.quantity)
            }
            tvquantityproduct.text = productItem.quantity.toString()



        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): PlacedOrderDetailsAdapter.MyViewHolder {
        return MyViewHolder(
            PlacedOrderPorductItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(
        holder: PlacedOrderDetailsAdapter.MyViewHolder, position: Int
    ) {
        holder.mBind(getItem(position))
    }



}

object PlacedOrderDetailsAdapterDiffCall : DiffUtil.ItemCallback<LineItemX>() {

    override fun areItemsTheSame(oldItem: LineItemX, newItem: LineItemX): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LineItemX, newItem: LineItemX): Boolean {
        return oldItem == newItem
    }

}