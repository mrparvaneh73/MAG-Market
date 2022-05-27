package com.example.magmarket.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.magmarket.data.model.CategoryItem
import com.example.magmarket.databinding.CategoryItemBinding

class CategoryAdapter(private var clickListener: (CategoryItem) -> Unit) :
    ListAdapter<CategoryItem, CategoryAdapter.MyViewHolder>(CategoryDiffCall) {

    inner class MyViewHolder(
        private val binding: CategoryItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun mBind(categoryItem: CategoryItem) = binding.apply {
            Glide.with(root)
                .load(categoryItem.image.src)
                .into(categoryImg)
            catrgoryName.text = categoryItem.name

            root.setOnClickListener {
                clickListener(categoryItem)
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): CategoryAdapter.MyViewHolder {
        return MyViewHolder(
            CategoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(
        holder: CategoryAdapter.MyViewHolder, position: Int
    ) {
        holder.mBind(getItem(position))
    }


}

object CategoryDiffCall : DiffUtil.ItemCallback<CategoryItem>() {

    override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
        return oldItem == newItem
    }

}