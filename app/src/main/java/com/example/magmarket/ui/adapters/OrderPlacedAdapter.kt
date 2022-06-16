package com.example.magmarket.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.magmarket.data.remote.model.ProductItem
import com.example.magmarket.data.remote.model.order.ResponseOrder
import com.example.magmarket.databinding.PlacedOrderItemBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class OrderPlacedAdapter(private var clickListener: (ResponseOrder) -> Unit) :
    ListAdapter<ResponseOrder, OrderPlacedAdapter.MyViewHolder>(PlacedOrderDiffCall) {
    val nf: NumberFormat = NumberFormat.getInstance(Locale.US)
    @SuppressLint("SimpleDateFormat")
    val dateFormat =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

    inner class MyViewHolder(
        private val binding: PlacedOrderItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun mBind(order: ResponseOrder) = binding.apply {
            tvOrderId.text=order.id.toString()
            tvPrice.text=nf.format(order.total.toInt())
            root.setOnClickListener {
                clickListener(order)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): OrderPlacedAdapter.MyViewHolder {
        return MyViewHolder(
            PlacedOrderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(
        holder: OrderPlacedAdapter.MyViewHolder, position: Int
    ) {
        holder.mBind(getItem(position))
    }


}

object PlacedOrderDiffCall : DiffUtil.ItemCallback<ResponseOrder>() {

    override fun areItemsTheSame(oldItem: ResponseOrder, newItem: ResponseOrder): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ResponseOrder, newItem: ResponseOrder): Boolean {
        return oldItem == newItem
    }

}