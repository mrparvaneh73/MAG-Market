package com.example.magmarket.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.magmarket.data.model.CategoryItem
import com.example.magmarket.data.model.ProductItem
import com.example.magmarket.databinding.CategoryItemBinding
import com.example.magmarket.databinding.ItemsSubCategoryBinding

class ProductsOfCategoryAdapter() :
    ListAdapter<ProductItem, ProductsOfCategoryAdapter.MyViewHolder>(ProductOfCategoryDiffCall) {

    inner class MyViewHolder(
        private val binding: ItemsSubCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun mBind(productItem: ProductItem) = binding.apply {
            Glide.with(root)
                .load(productItem.images[0].src)
                .into(productImage)
            productName.text = productItem.name
            productPrice.text = productItem.price


        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ProductsOfCategoryAdapter.MyViewHolder {
        return MyViewHolder(
            ItemsSubCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(
        holder: ProductsOfCategoryAdapter.MyViewHolder, position: Int
    ) {
        holder.mBind(getItem(position))
    }


}

object ProductOfCategoryDiffCall : DiffUtil.ItemCallback<ProductItem>() {

    override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
        return oldItem == newItem
    }

}