package com.xter.slimnotek.ui.note

import androidx.lifecycle.*
import com.xter.slimnotek.data.Note
import com.xter.slimnotek.data.NoteSource
import com.xter.slimnotek.util.L
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
        if (update) {
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

    private val mStates: LiveData<HashMap<Note, State>> = MutableLiveData(HashMap())
    val states = mStates

    private val mSelectedPosition = MutableLiveData<Int>()
    val selectedPosition = mSelectedPosition

    init {
        mUpdate.value = true
    }

    /**
     * 刷新
     */
    fun refresh() {
        mUpdate.value = true
    }

    fun remove(pos: Int) {
        items.value?.get(pos)?.let { note ->
            viewModelScope.launch {
                noteSource.deleteNote(note.id)
            }
        }
    }

    fun selected(pos: Int): State? {
        var state: State? = null
        items.value?.get(pos)?.let { note ->
            mStates.value?.let { states ->
                if (states.containsKey(note)) {
                    state = states.get(note)
                    state?.selected = state?.selected!!.not()
                } else {
                    state = State(pos, true)
                    states.put(note, state!!)
                }
            }
        }
        mSelectedPosition.value = pos
        return state
    }

    fun selectNote(note:Note):Boolean{
        L.d("note:$note")
        return false
    }

    fun remove() {

    }

    inner class State constructor(var position: Int, var selected: Boolean){
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as State

            if (position != other.position) return false

            return true
        }

        override fun hashCode(): Int {
            return position
        }

        override fun toString(): String {
            return "State(position=$position, selected=$selected)"
        }
    }
}
