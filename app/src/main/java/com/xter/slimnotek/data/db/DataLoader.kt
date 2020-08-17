package com.xter.slimnotek.data.db

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
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
        ).build()
        database = result
        return result
    }
}