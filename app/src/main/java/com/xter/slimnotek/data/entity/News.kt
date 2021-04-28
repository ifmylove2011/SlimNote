package com.xter.slimnotek.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * @Author XTER
 * @Date 2021/4/28 10:00
 * @Description
 * 聚合数据-新闻头条
 */
@Entity(tableName = "news")
data class News(
    @PrimaryKey
    @ColumnInfo(name = "newsId")
    @SerializedName("uniquekey")
    var uniquekey: String,
    var title: String,
    var date: String,
    var category: String,

    @SerializedName("author_name")
    var authorName: String,
    var url: String,

    @SerializedName("thumbnail_pic_s")
    var thumbnailPic: String,

    @SerializedName("is_content")
    var isContent: String
) {
    /**
     * uniquekey : 4a8a322572c93e2655a9f60075652fb6
     * title : 郑州彩虹桥拆解工程进展如何？
     * date : 2021-04-28 09:30:00
     * category : 头条
     * author_name : 浅语花开
     * url : https://mini.eastday.com/mobile/210428093015780541174.html
     * thumbnail_pic_s : https://dfzximg02.dftoutiao.com/minimodify/20210428/2000x1500_6088baa7414c0_mwpm_03201609.jpg
     * is_content : 1
     */

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as News

        if (uniquekey != other.uniquekey) return false

        return true
    }

    override fun hashCode(): Int {
        return uniquekey.hashCode()
    }

    override fun toString(): String {
        return "News(title=$title, date=$date, category=$category, authorName=$authorName, url=$url, thumbnailPic=$thumbnailPic, isContent=$isContent)"
    }

}