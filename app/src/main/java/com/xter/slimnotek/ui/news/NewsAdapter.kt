package com.xter.slimnotek.ui.news

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xter.slimnotek.data.entity.News
import com.xter.slimnotek.databinding.ItemNewsBinding

class NewsAdapter(private val viewModel: NewsViewModel) :
    ListAdapter<News, ViewHolderK>(NewsDiffCallback()) {

    private lateinit var onItemClickListener: OnItemClickListener

    fun setItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderK {
        return ViewHolderK.from(parent)
    }

    override fun onBindViewHolder(holderK: ViewHolderK, position: Int) {
        holderK.apply {
            val news = getItem(position)
            bind(viewModel, news)
            itemView.let { view ->
                view.setOnClickListener {
                    onItemClickListener.onItemClick(holderK, holderK.adapterPosition)
                }
                view.setOnLongClickListener {
                    onItemClickListener.onItemLongClick(holderK, holderK.adapterPosition)
                    true
                }
            }
        }
    }
}

class ViewHolderK private constructor(val binding: ItemNewsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(vm: NewsViewModel, item: News) {
        binding.apply {
            this.newsViewModel = vm
            this.news = item
            this.executePendingBindings()
        }
    }

    companion object {
        fun from(parent: ViewGroup): ViewHolderK =
            parent.let {
                val binding =
                    ItemNewsBinding.inflate(LayoutInflater.from(it.context), it, false)
                ViewHolderK(binding)
            }
    }
}

interface OnItemClickListener {
    fun onItemClick(holderK: ViewHolderK, position: Int)
    fun onItemLongClick(holderK: ViewHolderK, position: Int)
}

class NewsDiffCallback : DiffUtil.ItemCallback<News>() {
    override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem == newItem
    }
}

@BindingAdapter("items")
fun setItems(recyclerView: RecyclerView, items: List<News>) {
    (recyclerView.adapter as NewsAdapter).submitList(items)
}

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String) {
    Glide.with(view.context).load(url).into(view)
}
