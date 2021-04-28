package com.xter.slimnotek.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xter.slimnotek.data.entity.Note
import com.xter.slimnotek.data.NoteSource
import com.xter.slimnotek.util.Utils
import kotlinx.coroutines.launch

class NoteDetailViewModel constructor(private val noteSource: NoteSource) : ViewModel() {

    val title = MutableLiveData<String>()

    val content = MutableLiveData<String>()

    val editable = MutableLiveData<Boolean>(false)

    private val mSnackbarText = MutableLiveData<String>()
    val snackbarText: LiveData<String> = mSnackbarText

    private lateinit var note: Note

    fun load(noteid: String) {
        viewModelScope.launch {
            note = noteSource.getNote(noteid)!!
            note
                .apply {
                    lastViewTime = Utils.getNormalDate1()
                }
                .let { note ->
                    title.value = note.title
                    content.value = note.content
                    viewModelScope.launch {
                        noteSource.updateNote(note)
                    }
                }
        }
    }

    fun edit() {
        editable.value = true
    }

    fun updateAndSave(title: String, content: String) {
        if (title.isEmpty()) {
            mSnackbarText.value = "标题不可为空"
            return
        }
        note.apply {
            this.title = title
            this.content = content
            val updateTime = Utils.getNormalDate1()
            this.updateTime = updateTime
            this.lastViewTime = updateTime
        }.let {
            viewModelScope.launch {
                noteSource.updateNote(it)
            }
            editable.value = false
        }
    }
}
