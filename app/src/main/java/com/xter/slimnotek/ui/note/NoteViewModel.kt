package com.xter.slimnotek.ui.note

import androidx.lifecycle.*
import com.xter.slimnotek.data.Note
import com.xter.slimnotek.data.NoteSource
import kotlinx.coroutines.launch

class NoteViewModel(
    private val noteSource: NoteSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mUpdate = MutableLiveData<Boolean>(false)

    /**
     * 内容项
     */
    private val mItems: LiveData<List<Note>> = mUpdate.switchMap { update ->
        if(update){
            mDataLoading.value = true
            viewModelScope.launch {
                noteSource.refreshNotes()
                mDataLoading.value = false
            }
        }
        noteSource.observeNotes().distinctUntilChanged()
    }
    val items: LiveData<List<Note>> = mItems

    /**
     * 加载中
     */
    private val mDataLoading = MutableLiveData<Boolean>(false)
    val dataLoading: LiveData<Boolean> = mDataLoading

    /**
     * 是否为空
     */
    val empty: LiveData<Boolean> = mItems.map {
        it.isEmpty()
    }

    init {
        mUpdate.value = true
    }

    /**
     * 刷新
     */
    fun refresh(){
        mUpdate.value = true
    }
}
