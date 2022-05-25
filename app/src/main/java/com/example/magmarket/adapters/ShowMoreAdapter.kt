package com.example.magmarket.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.magmarket.data.model.ProductItem
import com.example.magmarket.data.model.ProductRecyclerViewItem
import com.example.magmarket.databinding.ItemsSubCategoryBinding

class ShowMoreAdapter(private var clickListener: (ProductRecyclerViewItem.ProductItem) -> Unit) :
    ListAdapter<ProductRecyclerViewItem.ProductItem, ShowMoreAdapter.MyViewHolder>(ShowMoreOfCategoryDiffCall) {

    inner class MyViewHolder(
        private val binding: ItemsSubCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun mBind(productItem: ProductRecyclerViewItem.ProductItem) = binding.apply {
            Glide.with(root)
                .load(productItem.images[0].src)
                .into(productImage)
            productName.text = productItem.name
            productPrice.text = productItem.price

            root.setOnClickListener {
                clickListener(productItem)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ShowMoreAdapter.MyViewHolder {
        return MyViewHolder(
            ItemsSubCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(
        holder: ShowMoreAdapter.MyViewHolder, position: Int
    ) {
        holder.mBind(getItem(position))
    }


}

object ShowMoreOfCategoryDiffCall : DiffUtil.ItemCallback<ProductRecyclerViewItem.ProductItem>() {

    override fun areItemsTheSame(oldItem: ProductRecyclerViewItem.ProductItem, newItem: ProductRecyclerViewItem.ProductItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ProductRecyclerViewItem.ProductItem, newItem: ProductRecyclerViewItem.ProductItem): Boolean {
        return oldItem == newItem
    }

}