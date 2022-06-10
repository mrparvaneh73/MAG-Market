package com.example.magmarket.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.magmarket.R
import com.example.magmarket.data.remote.model.ProductRecyclerViewItem
import com.example.magmarket.databinding.ItemsSubCategoryBinding
import java.text.NumberFormat
import java.util.*

class ShowMoreAdapter(private var clickListener: (ProductRecyclerViewItem.ProductItem) -> Unit) :
    PagingDataAdapter<ProductRecyclerViewItem.ProductItem, ShowMoreAdapter.MyViewHolder>(ShowMoreOfCategoryDiffCall) {
    val nf: NumberFormat = NumberFormat.getInstance(Locale.US)
    inner class MyViewHolder(
        private val binding: ItemsSubCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun mBind(productItem: ProductRecyclerViewItem.ProductItem) = binding.apply {
            if (productItem.images!!.isNotEmpty()){
                Glide.with(root)
                    .load(productItem.images.get(0).src)
                    .placeholder(R.drawable.emptyimage)
                    .into(productImage)
            }
       if (!productItem.name.isNullOrEmpty()){

           productName.text = productItem.name
       }else{
           productName.text="بی نام"
       }
            if (!productItem.price.isNullOrEmpty()){

                productPrice.text = nf.format(productItem.price.toInt())

            }else{
                productPrice.text="فاقد قیمت "
            }
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
        getItem(position)?.let { holder.mBind(it) }
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