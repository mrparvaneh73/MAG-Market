package com.example.magmarket.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.magmarket.R
import com.example.magmarket.data.remote.model.ProductRecyclerViewItem
import com.example.magmarket.databinding.ItemTitleBinding
import com.example.magmarket.databinding.ProductItemBinding
import com.example.magmarket.databinding.ShowAllBinding


class ProductRecyclerviewAdapter : RecyclerView.Adapter<ProductRecyclerviewHolder>() {

    var items = listOf<ProductRecyclerViewItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var itemClickListener: ((view: View, item: ProductRecyclerViewItem, position: Int) -> Unit)? =
        null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductRecyclerviewHolder {
        return when (viewType) {
            R.layout.item_title -> ProductRecyclerviewHolder.TitleViewHolder(
                ItemTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            R.layout.product_item -> ProductRecyclerviewHolder.MyViewHolder(
                ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            R.layout.show_all -> ProductRecyclerviewHolder.ShowAllViewHolder(
                ShowAllBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> throw IllegalArgumentException("Invalid ViewType Exception")
        }
    }

    override fun onBindViewHolder(holder: ProductRecyclerviewHolder, position: Int) {
        holder.itemClickListener=itemClickListener
        when (holder) {
            is ProductRecyclerviewHolder.TitleViewHolder -> holder.bind(items[position] as ProductRecyclerViewItem.HeaderProductTitle)
            is ProductRecyclerviewHolder.MyViewHolder -> holder.bind(items[position] as ProductRecyclerViewItem.ProductItem)
            is ProductRecyclerviewHolder.ShowAllViewHolder -> holder.bind(items[position] as ProductRecyclerViewItem.ShowAll)

        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ProductRecyclerViewItem.HeaderProductTitle -> R.layout.item_title
            is ProductRecyclerViewItem.ProductItem -> R.layout.product_item
            is ProductRecyclerViewItem.ShowAll -> R.layout.show_all
        }
    }
}