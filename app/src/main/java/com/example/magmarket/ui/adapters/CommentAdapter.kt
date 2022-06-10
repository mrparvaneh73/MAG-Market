package com.example.magmarket.ui.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.magmarket.data.remote.model.Cart
import com.example.magmarket.data.remote.model.review.ResponseReview
import com.example.magmarket.databinding.CommentItemBinding

class CommentAdapter() :
    ListAdapter<ResponseReview, CommentAdapter.CommentViewHolder>(CommentDiffCall) {

    inner class CommentViewHolder(private val binding: CommentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
     fun bind(commentItem: ResponseReview) = with(binding) {
            username.text = commentItem.reviewer
            commentUser.text = HtmlCompat.fromHtml(commentItem.review, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            CommentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
       holder.bind(getItem(position))
    }
}

object CommentDiffCall : DiffUtil.ItemCallback<ResponseReview>() {

    override fun areItemsTheSame(oldItem: ResponseReview, newItem: ResponseReview): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ResponseReview, newItem: ResponseReview): Boolean {
        return oldItem == newItem
    }

}