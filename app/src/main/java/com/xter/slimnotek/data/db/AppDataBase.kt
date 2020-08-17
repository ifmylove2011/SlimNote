package com.xter.slimnotek.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xter.slimnotek.data.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}