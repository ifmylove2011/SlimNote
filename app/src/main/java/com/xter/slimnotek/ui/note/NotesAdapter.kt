package com.xter.slimnotek.ui.note

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xter.slimnotek.data.Note
import com.xter.slimnotek.databinding.ItemNoteBinding
import com.xter.slimnotek.util.L

class NotesAdapter(private val viewModel: NoteViewModel) :
    ListAdapter<Note, ViewHolder>(NoteDiffCallback()) {

    private lateinit var onItemClickListener: OnItemClickListener

    fun setItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            bind(viewModel, getItem(position))
            itemView.let {
                it.setOnClickListener { view ->
                    onItemClickListener.onItemClick(view, holder.adapterPosition)
                }
                it.setOnLongClickListener { view ->
                    onItemClickListener.onItemLongClick(view, holder.adapterPosition)
                    true
                }
            }
        }
    }
}

class ViewHolder private constructor(val binding: ItemNoteBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(vm: NoteViewModel, item: Note) {
        binding.apply {
            this.noteViewModel = vm
            this.note = item
            this.executePendingBindings()
        }
    }

    companion object {
        fun from(parent: ViewGroup): ViewHolder =
            parent.let {
                val binding =
                    ItemNoteBinding.inflate(LayoutInflater.from(it.context), it, false)
                ViewHolder(binding)
            }
    }
}

interface OnItemClickListener {
    fun onItemClick(view: View, position: Int)
    fun onItemLongClick(view: View, position: Int)
}

class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }

}

@BindingAdapter("items")
fun setItems(recyclerView: RecyclerView, items: List<Note>) {
    (recyclerView.adapter as NotesAdapter).submitList(items)
}