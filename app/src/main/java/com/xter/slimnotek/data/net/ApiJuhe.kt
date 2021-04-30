package com.xter.slimnotek.data.net

import com.xter.slimnotek.data.BaseJuheResponse
import com.xter.slimnotek.data.NewsCase
import com.xter.slimnotek.data.entity.News
import com.xter.slimnotek.extension.Constant
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @Author XTER
 * @Date 2021/4/28 14:14
 * @Description
 * 聚合数据
 */
interface ApiJuhe {

    @GET("/toutiao/index")
    fun getNews(
        @Query("type") type: String = "top",
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 15,
        @Query("key") appKey: String = Constant.KEY_JUHE_NEWS
    ): Observable<BaseJuheResponse<NewsCase<List<News>>>>
}