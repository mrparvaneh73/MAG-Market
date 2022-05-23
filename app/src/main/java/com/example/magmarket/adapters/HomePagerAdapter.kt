package com.example.magmarket.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.magmarket.R
import com.example.magmarket.data.model.ProductImage
import com.example.magmarket.databinding.SliderItemBinding

class HomePagerAdapter(private val images: List<Int>) :
    RecyclerView.Adapter<HomePagerAdapter.SliderViewHolder>() {

    inner class SliderViewHolder(var binding: SliderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: Int) = binding.apply {
            binding.productImg.setBackgroundResource(image)
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
        holder.bind(images[position])

    }


    override fun getItemCount(): Int = images.size
}



