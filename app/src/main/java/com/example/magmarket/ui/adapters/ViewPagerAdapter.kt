package com.example.magmarket.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.magmarket.R
import com.example.magmarket.data.remote.model.ProductImage
import java.util.ArrayList

class ViewPagerAdapter(
    private val imageList: ArrayList<ProductImage>,
    private val viewPager2: ViewPager2
) : RecyclerView.Adapter<ViewPagerAdapter.ImageViewHolder>() {
    class ImageViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val imageView = itemview.findViewById<ImageView>(R.id.product_img)
        fun bind(imag:ProductImage){
            Glide.with(itemView).load(imag.src).into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.slider_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
       holder.bind(imageList[position]).apply {
           if (position==imageList.size-1){
               viewPager2.post(runnable)
           }
       }

    }

    override fun getItemCount(): Int = imageList.size
    private val runnable= Runnable {
        imageList.addAll(imageList)
        notifyDataSetChanged()
    }

}