package com.example.magmarket.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.magmarket.data.model.ProductImage
import com.example.magmarket.databinding.SliderItemBinding

class SliderAdapter :
    ListAdapter<ProductImage, SliderAdapter.SliderViewHolder>(ImageDiffCall) {

    inner class SliderViewHolder(var binding: SliderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(productImage: ProductImage) = binding.apply {
           Glide.with(root).load(productImage.src).into(productImg)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(
            SliderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.apply {
            holder.bind(getItem(position))

        }


    }

}

object ImageDiffCall : DiffUtil.ItemCallback<ProductImage>() {

    override fun areItemsTheSame(oldItem: ProductImage, newItem: ProductImage): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductImage, newItem: ProductImage): Boolean {
        return oldItem == newItem
    }

}