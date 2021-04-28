package com.xter.slimnotek.data

/**
 * @Author XTER
 * @Date 2021/4/28 15:26
 * @Description
 */
data class NewsCase<T>(
    var stat: String,
    var page: Int,
    var pageSize: Int,
    var data: T
)
