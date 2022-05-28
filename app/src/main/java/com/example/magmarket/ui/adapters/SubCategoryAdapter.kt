package com.example.magmarket.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.magmarket.data.remote.model.CategoryItem
import com.example.magmarket.databinding.SubCategoryItemBinding

class SubCategoryAdapter(private var clickListener: (CategoryItem) -> Unit) :
    ListAdapter<CategoryItem, SubCategoryAdapter.MyViewHolder>(SubCategoryDiffCall) {

    inner class MyViewHolder(
        private val binding: SubCategoryItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun mBind(categoryItem: CategoryItem) = binding.apply {
            Glide.with(root)
                .load(categoryItem.image.src)
                .into(imgCategory)
            tvCategoryName.text = categoryItem.name
            count.text = "+ ${categoryItem.count}  "
            root.setOnClickListener {
                clickListener(categoryItem)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): SubCategoryAdapter.MyViewHolder {
        return MyViewHolder(
            SubCategoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(
        holder: SubCategoryAdapter.MyViewHolder, position: Int
    ) {
        holder.mBind(getItem(position))
    }


}

object SubCategoryDiffCall : DiffUtil.ItemCallback<CategoryItem>() {

    override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
        return oldItem == newItem
    }

}