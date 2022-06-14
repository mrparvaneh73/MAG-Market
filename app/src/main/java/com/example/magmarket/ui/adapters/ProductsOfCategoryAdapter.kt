package com.example.magmarket.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.magmarket.R
import com.example.magmarket.data.remote.model.ProductItem
import com.example.magmarket.data.remote.model.ProductRecyclerViewItem
import com.example.magmarket.databinding.ItemsSubCategoryBinding
import java.text.NumberFormat
import java.util.*

class ProductsOfCategoryAdapter(private var clickListener: (ProductItem) -> Unit) :
    ListAdapter<ProductItem, ProductsOfCategoryAdapter.MyViewHolder>(ProductOfCategoryDiffCall) {
    val nf: NumberFormat = NumberFormat.getInstance(Locale.US)
    inner class MyViewHolder(
        private val binding: ItemsSubCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun mBind(productItem: ProductItem) = binding.apply {
            if (!productItem.images.isNullOrEmpty()){
                Glide.with(root)
                    .load(productItem.images[0].src)
                    .placeholder(R.drawable.emptyimage)
                    .into(productImage)
            }
       if (!productItem.name.isNullOrEmpty()){

           productName.text = productItem.name
       }else{
           productName.text="محصول فاقد نام"
       }
            if (!productItem.price.isNullOrEmpty()){
                productPrice.text = nf.format(productItem.price.toInt())
            }else {
                productPrice.text="بدون قیمت"
            }


            root.setOnClickListener {
                clickListener(productItem)
            }
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