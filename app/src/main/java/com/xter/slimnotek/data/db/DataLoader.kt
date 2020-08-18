package com.xter.slimnotek.data.db

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.xter.slimnotek.data.NoteLocalSource
import com.xter.slimnotek.data.NoteRemoteSource
import com.xter.slimnotek.data.NoteSource
import com.xter.slimnotek.data.NoteSourceManager

object DataLoader {

    private val lock = Any()
    private var database: AppDataBase? = null

    @Volatile
    var noteSource: NoteSource? = null
        @VisibleForTesting set

    fun provideNoteSource(context: Context): NoteSource {
        synchronized(this) {
            return noteSource ?: noteSource ?: createNoteSource(context)
        }
    }

    private fun createNoteSource(context: Context): NoteSource {
        val firstNoteSource = NoteSourceManager(NoteRemoteSource(), createNoteLocalSource(context))
        noteSource = firstNoteSource
        return firstNoteSource
    }

    private fun createNoteLocalSource(context: Context): NoteSource {
        val database = database ?: createDatabase(context)
        return NoteLocalSource(database.noteDao())
    }

    private fun createDatabase(context: Context): AppDataBase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            AppDataBase::class.java, "notes.db"
        )
            .addMigrations(migration1_2)
            .build()
        database = result
        return result
    }

    val migration1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            //备份
            database.execSQL("CREATE TABLE IF NOT EXISTS note_new (title TEXT NOT NULL, content TEXT, abstractContent TEXT, createTime TEXT NOT NULL, updateTime TEXT NOT NULL, lastViewTime TEXT NOT NULL, noteid TEXT NOT NULL, PRIMARY KEY(noteid))")
            //复制数据
            database.execSQL(
                "INSERT INTO note_new (noteid , title, content, abstractContent, createTime, updateTime, lastViewTime) SELECT noteid , title, content, abstractContent, createTime, updateTime, lastViewTime FROM note"
            )
            //删除旧表
            database.execSQL("DROP TABLE note")
            //重命名新表
            database.execSQL("ALTER TABLE note_new RENAME TO note")
        }

    }
}