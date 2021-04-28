package com.xter.slimnotek.data

/**
 * @Author XTER
 * @Date 2021/4/28 14:15
 * @Description
 */
data class BaseJuheResponse<T>(
    var reason: String,
    var errorCode: Int,
    var result: T
)