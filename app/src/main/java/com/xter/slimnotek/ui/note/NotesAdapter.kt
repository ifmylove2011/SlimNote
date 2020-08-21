package com.xter.slimnotek.ui.note

import android.graphics.Color
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
    ListAdapter<Note, ViewHolderK>(NoteDiffCallback()) {

    private lateinit var onItemClickListener: OnItemClickListener

    fun setItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderK {
        return ViewHolderK.from(parent)
    }

    override fun onBindViewHolder(holderK: ViewHolderK, position: Int) {
        holderK.apply {
            val note = getItem(position)
            bind(viewModel, note)
            itemView.let { view ->
                view.setOnClickListener {
                    onItemClickListener.onItemClick(holderK, holderK.adapterPosition)
                }
                view.setOnLongClickListener {
                    onItemClickListener.onItemLongClick(holderK, holderK.adapterPosition)
                    true
                }
                val state = viewModel.states.value?.get(note)
                if (state == null) {
                    view.setBackgroundColor(Color.TRANSPARENT)
                } else {
                    if (state.selected) {
                        view.setBackgroundColor(Color.LTGRAY)
                    } else {
                        view.setBackgroundColor(Color.TRANSPARENT)
                    }
                }
            }
        }
    }
}

class ViewHolderK private constructor(val binding: ItemNoteBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(vm: NoteViewModel, item: Note) {
        binding.apply {
            this.noteViewModel = vm
            this.note = item
            this.executePendingBindings()
        }
    }

    companion object {
        fun from(parent: ViewGroup): ViewHolderK =
            parent.let {
                val binding =
                    ItemNoteBinding.inflate(LayoutInflater.from(it.context), it, false)
                ViewHolderK(binding)
            }
    }
}

interface OnItemClickListener {
    fun onItemClick(holderK: ViewHolderK, position: Int)
    fun onItemLongClick(holderK: ViewHolderK, position: Int)
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