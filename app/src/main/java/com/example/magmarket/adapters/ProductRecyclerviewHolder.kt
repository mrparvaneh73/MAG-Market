package com.example.magmarket.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.magmarket.R
import com.example.magmarket.data.model.ProductRecyclerViewItem
import com.example.magmarket.databinding.ItemTitleBinding
import com.example.magmarket.databinding.ProductItemBinding
import com.example.magmarket.databinding.ShowAllBinding

sealed class ProductRecyclerviewHolder(binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var itemClickListener: ((view: View, item: ProductRecyclerViewItem, position: Int) -> Unit)? =
        null

    class TitleViewHolder(private val binding: ItemTitleBinding) :
        ProductRecyclerviewHolder(binding) {
        fun bind(image: ProductRecyclerViewItem.HeaderProductTitle) = binding.apply {
            Glide.with(root).load(image.title).placeholder(R.drawable.newicon)
                .into(imgProduct)

            root.setOnClickListener {
                itemClickListener!!.invoke(it, image, absoluteAdapterPosition)
            }
        }
    }

    class ShowAllViewHolder(private val binding: ShowAllBinding) :
        ProductRecyclerviewHolder(binding) {
        fun bind(title: ProductRecyclerViewItem.ShowAll) = binding.apply {
            tvshowall.text = title.title

            root.setOnClickListener {
                itemClickListener!!.invoke(it, title, absoluteAdapterPosition)
            }
        }
    }

    class MyViewHolder(private val binding: ProductItemBinding) :
        ProductRecyclerviewHolder(binding) {

        fun bind(productItem: ProductRecyclerViewItem.ProductItem) = binding.apply {
            Glide.with(root)
                .load(productItem.images[0].src)
                .into(imgProduct)
            tvnameproduct.text = productItem.name
            tvpriceproduct.text = productItem.price

            root.setOnClickListener {
                itemClickListener!!.invoke(it, productItem, absoluteAdapterPosition)
            }
        }
    }

}
