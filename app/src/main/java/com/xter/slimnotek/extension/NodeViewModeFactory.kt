package com.xter.slimnotek.extension

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.xter.slimnotek.data.NoteSource
import com.xter.slimnotek.ui.news.NewsViewModel
import com.xter.slimnotek.ui.note.AddNoteViewModel
import com.xter.slimnotek.ui.note.NoteDetailViewModel
import com.xter.slimnotek.ui.note.NoteViewModel

@Suppress("UNCHECKED_CAST")
class NodeViewModeFactory constructor(
    private val noteSource: NoteSource,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return modelClass.let {
            when {
                it.isAssignableFrom(NoteViewModel::class.java) -> NoteViewModel(noteSource, handle)
                it.isAssignableFrom(AddNoteViewModel::class.java) -> AddNoteViewModel(noteSource)
                it.isAssignableFrom(NoteDetailViewModel::class.java) -> NoteDetailViewModel(noteSource)
                it.isAssignableFrom(NewsViewModel::class.java) -> NewsViewModel(noteSource, handle)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            } as T
        }
    }
}