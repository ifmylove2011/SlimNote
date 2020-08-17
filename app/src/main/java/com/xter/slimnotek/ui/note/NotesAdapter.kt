package com.xter.slimnotek.ui.note

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xter.slimnotek.data.Note
import com.xter.slimnotek.databinding.ItemNoteBinding

class NotesAdapter(private val viewModel: NoteViewModel) :
    ListAdapter<Note, ViewHolder>(NoteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            bind(viewModel, getItem(position))
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

class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }

}

@BindingAdapter("items")
fun setItems(listView: RecyclerView, items: List<Note>) {
    (listView.adapter as NotesAdapter).submitList(items)
}