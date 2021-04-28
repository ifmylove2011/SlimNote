package com.xter.slimnotek.util

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import java.text.SimpleDateFormat
import java.util.*

object Utils {


    private val NORMAL_DATE_1: ThreadLocal<SimpleDateFormat> =
        object : ThreadLocal<SimpleDateFormat>() {
            override fun initialValue(): SimpleDateFormat {
                return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
            }
        }


    private val NORMAL_DATE_2: ThreadLocal<SimpleDateFormat> =
        object : ThreadLocal<SimpleDateFormat>() {
            override fun initialValue(): SimpleDateFormat {
                return SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
            }
        }

    private val NORMAL_DATE_3: ThreadLocal<SimpleDateFormat> =
        object : ThreadLocal<SimpleDateFormat>() {
            override fun initialValue(): SimpleDateFormat {
                return SimpleDateFormat("yyyyMMddHHmmss.SSS", Locale.CHINA)
            }
        }

    private val NORMAL_TIME_1: ThreadLocal<SimpleDateFormat> =
        object : ThreadLocal<SimpleDateFormat>() {
            override fun initialValue(): SimpleDateFormat {
                return SimpleDateFormat("HH:mm", Locale.CHINA)
            }
        }

    fun getNormalDate1(): String {
        return NORMAL_DATE_1.get()!!.format(System.currentTimeMillis())
    }

    fun getNormalTime1(): String {
        return NORMAL_TIME_1.get()!!.format(System.currentTimeMillis())
    }
}