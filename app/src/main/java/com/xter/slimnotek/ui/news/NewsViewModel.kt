package com.xter.slimnotek.ui.news

import android.os.Looper
import androidx.lifecycle.*
import com.xter.slimnotek.data.NoteSource
import com.xter.slimnotek.data.entity.News
import com.xter.slimnotek.util.L
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class NewsViewModel(
    private val noteSource: NoteSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mNews = MutableLiveData<List<News>>(emptyList())
    val news: LiveData<List<News>> = mNews

    /**
     * 加载中
     */
    private val mDataLoading = MutableLiveData<Boolean>(false)
    val dataLoading: LiveData<Boolean> = mDataLoading

    val empty: LiveData<Boolean> = mNews.map {
        it.isEmpty()
    }

    fun refresh() {
        viewModelScope.launch {
            noteSource.getNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { response ->
                    if (response.errorCode == 0) {
                        mNews.value = response.result.data
                        L.i("news ${response.result.pageSize}")
                        mDataLoading.value = false
                    } else {
                        L.w(response.reason)
                    }
                }
        }

    }

}