package com.xter.slimnotek.data.net

import com.xter.slimnotek.extension.Constant
import com.xter.slimnotek.util.L
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @Author XTER
 * @Date 2021/4/28 13:56
 * @Description
 */
class RetrofitHelper private constructor() {

    enum class DATA {
        JISU,
        JUHE,
        CUSTOM
    }

    companion object {
        val INSTANCE = Holder.holder

        private const val DEFAULT_TIME = 10L
    }

    private object Holder {
        val holder = RetrofitHelper()
    }

    private val okHttpClient: OkHttpClient
    private val serviceJuhe: ApiJuhe
    private val serviceJisu: ApiJisu

    init {
        okHttpClient = OkHttpClient()
            .newBuilder()
            .connectTimeout(DEFAULT_TIME, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIME, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIME, TimeUnit.SECONDS)
            .addInterceptor(LogInterceptor())
            .build()
        val retrofitJuhe = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Constant.HEAD_JUHE)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitJisu = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Constant.HEAD_JISU)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        serviceJuhe = retrofitJuhe.create(ApiJuhe::class.java)
        serviceJisu = retrofitJisu.create(ApiJisu::class.java)
    }

    fun getJuHe(): ApiJuhe {
        return serviceJuhe
    }

    fun getJisu(): ApiJisu {
        return serviceJisu
    }

    private inner class LogInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request: Request = chain.request()
            L.i(request.toString())
            return chain.proceed(request)
        }

    }
}