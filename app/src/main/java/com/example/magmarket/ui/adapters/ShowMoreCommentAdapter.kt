package com.example.magmarket.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.magmarket.R
import com.example.magmarket.data.remote.model.ProductRecyclerViewItem
import com.example.magmarket.data.remote.model.review.ResponseReview
import com.example.magmarket.databinding.ItemsSubCategoryBinding
import com.example.magmarket.databinding.ShowAllCommentItemBinding
import java.text.NumberFormat
import java.util.*

class ShowMoreCommentAdapter() :
    PagingDataAdapter<ResponseReview, ShowMoreCommentAdapter.MyViewHolder>(ShowMoreCommentDiffCall) {
    val nf: NumberFormat = NumberFormat.getInstance(Locale.US)
    inner class MyViewHolder(
        private val binding: ShowAllCommentItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun mBind(commentItem: ResponseReview) = binding.apply {
            username.text = commentItem.reviewer
            commentUser.text =
                HtmlCompat.fromHtml(commentItem.review, HtmlCompat.FROM_HTML_MODE_LEGACY)
            ratingBar.rating = commentItem.rating.toFloat()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ShowMoreCommentAdapter.MyViewHolder {
        return MyViewHolder(
            ShowAllCommentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(
        holder: ShowMoreCommentAdapter.MyViewHolder, position: Int
    ) {
        getItem(position)?.let { holder.mBind(it) }
    }


}

object ShowMoreCommentDiffCall : DiffUtil.ItemCallback<ResponseReview>() {

    override fun areItemsTheSame(oldItem:ResponseReview, newItem: ResponseReview): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ResponseReview, newItem: ResponseReview): Boolean {
        return oldItem == newItem
    }

}