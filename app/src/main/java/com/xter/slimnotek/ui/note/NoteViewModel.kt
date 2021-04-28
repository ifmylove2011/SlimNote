package com.xter.slimnotek.ui.note

import androidx.lifecycle.*
import com.xter.slimnotek.data.entity.Note
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
        if (update) {
            mDataLoading.value = true
            viewModelScope.launch {
                noteSource.refreshNotes()
                mDataLoading.value = false
                if (mFirstFlag) {
                    mFirstFlag = false
                } else {
                    operationState.value = REFRESH_SUCCESS
                }
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

    /**
     * 选中状态
     */
    private val mStates: LiveData<HashMap<Note, State>> = MutableLiveData(HashMap())
    val states = mStates

    private val mSelectedNum = MutableLiveData<Int>(0)
    val selectedNum = mSelectedNum

    val operationState = MutableLiveData(0)

    private var mFirstFlag = true

    init {
        mUpdate.value = true
    }

    /**
     * 刷新
     */
    fun refresh() {
        mUpdate.value = true
    }

    /**
     * 选中某项，改变状态
     * @param pos 位置索引
     * @return state 当前状态
     */
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
        state?.let {
            if (it.selected) {
                if (mSelectedNum.value == -1) {
                    mSelectedNum.value = 1
                } else {
                    mSelectedNum.value = mSelectedNum.value?.plus(1)
                }
            } else {
                mSelectedNum.value = mSelectedNum.value?.minus(1)
            }
        }
        return state
    }

    fun selectedNotes(): List<Note>? {
        return mStates.value?.filterValues { state ->
            state.selected
        }?.keys?.toMutableList()
    }

    fun remove() {
        selectedNotes()?.let {
            viewModelScope.launch {
                noteSource.deleteNotes(it)
            }
            clearFocus()
        }
        operationState.value = REMOVE_SUCCESS
    }

    fun clearFocus() {
        mSelectedNum.value = -1
        mStates.value?.clear()
    }

    /*--------------------------- 状态类 -------------------------- */
    inner class State constructor(var position: Int, var selected: Boolean) {
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
