package com.example.magmarket.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.magmarket.data.model.ProductItem
import com.example.magmarket.databinding.ProductItemBinding


class ProductAdapter() : ListAdapter<ProductItem, ProductAdapter.MyViewHolder>(PictureDiffCall) {

    class MyViewHolder(
        private val binding:  ProductItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun mBind(productItem: ProductItem)=binding.apply {
            Glide.with(root)
                .load(productItem.images[0].src)
                .into(imgProduct)
            tvnameproduct.text=productItem.name
            tvpriceproduct.text=productItem.price
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ProductAdapter.MyViewHolder {
        return MyViewHolder(
            ProductItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(
        holder: ProductAdapter.MyViewHolder, position: Int
    ) {
        holder.mBind(getItem(position))
    }
}

object PictureDiffCall : DiffUtil.ItemCallback<ProductItem>() {

    override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
        return oldItem == newItem
    }

}