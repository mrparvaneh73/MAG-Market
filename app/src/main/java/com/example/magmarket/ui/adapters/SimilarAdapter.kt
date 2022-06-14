package com.example.magmarket.ui.adapters

import android.graphics.Paint
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
import com.example.magmarket.databinding.ProductItemBinding
import java.text.NumberFormat
import java.util.*

class SimilarAdapter(private var clickListener: (ProductItem) -> Unit) :
    ListAdapter<ProductItem, SimilarAdapter.MyViewHolder>(SimilarProductDiffCall) {
    val nf: NumberFormat = NumberFormat.getInstance(Locale.US)
    inner class MyViewHolder(
        private val binding: ProductItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun mBind(productItem: ProductItem) = binding.apply {
            if(!productItem.images.isNullOrEmpty()){
                Glide.with(root)
                    .load(productItem.images[0].src)
                    .placeholder(R.drawable.emptyimage)
                    .into(imgProduct)
            }
   if (!productItem.name.isNullOrEmpty()){
       tvnameproduct.text = productItem.name
   }else{
       tvnameproduct.text="محصول فاقد نام"
   }
      if (!productItem.price.isNullOrEmpty()){
          if (productItem.regular_price!!.toInt()==productItem.price.toInt()){
              regularprice.text=""
          }else{
              regularprice.text=  nf.format(productItem.regular_price.toInt())
              regularprice.paintFlags= Paint.STRIKE_THRU_TEXT_FLAG
          }

          tvpriceproduct.text = nf.format(productItem.price.toInt())
      }else{
          tvpriceproduct.text="محصول فاقد قیمت "
      }


            root.setOnClickListener {
                clickListener(productItem)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): SimilarAdapter.MyViewHolder {
        return MyViewHolder(
            ProductItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(
        holder: SimilarAdapter.MyViewHolder, position: Int
    ) {
        holder.mBind(getItem(position))
    }


}

object SimilarProductDiffCall : DiffUtil.ItemCallback<ProductItem>() {

    override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
        return oldItem == newItem
    }

}