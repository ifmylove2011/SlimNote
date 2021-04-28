package com.xter.slimnotek.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "note")
data class Note @JvmOverloads constructor(
    var title: String,
    var content: String?,
    var abstractContent: String? = "",
    var createTime: String,
    var updateTime: String,
    var lastViewTime: String,
    @PrimaryKey @ColumnInfo(name = "noteid") var id: String = UUID.randomUUID().toString()
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Note

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "Note(id='$id', title='$title', abstractContent=$abstractContent, updateTime='$updateTime')"
    }


}