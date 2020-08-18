package com.xter.slimnotek.data

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
)