package com.xter.slimnotek.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xter.slimnotek.data.entity.Note
import com.xter.slimnotek.data.NoteSource
import com.xter.slimnotek.util.Utils
import kotlinx.coroutines.launch

class AddNoteViewModel constructor(private val noteSource: NoteSource) : ViewModel() {

    val title = MutableLiveData<String>()

    val content = MutableLiveData<String>()

    private val mSnackbarText = MutableLiveData<String>()
    val snackbarText: LiveData<String> = mSnackbarText

    val completed = MutableLiveData<Boolean>(false)

    fun saveNote() {
        if (title.value.isNullOrEmpty()) {
            mSnackbarText.value = "标题不可为空"
            return
        }

        val firstDate = Utils.getNormalDate1()
        val con = content.value
        val abstractContent = getAbstract(con)
        val note = Note(
            title.value!!, content.value,
            abstractContent, firstDate, firstDate, firstDate
        )

        viewModelScope.launch {
            noteSource.addNote(note)
            completed.value = true
            mSnackbarText.value = "保存成功"
        }
    }

    fun getAbstract(src: String?): String? {
        if (src.isNullOrEmpty() || src.length < 20) {
            return src
        } else {
            return src.substring(0, 20)
        }
    }
}
