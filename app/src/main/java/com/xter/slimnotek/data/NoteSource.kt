package com.xter.slimnotek.data

import androidx.lifecycle.LiveData

interface NoteSource {

    fun observeNotes(): LiveData<List<Note>>

    fun observeNotes(noteid: String): LiveData<Note>

    suspend fun getNote(noteId: String): Note?

    suspend fun getNotes(title: String): List<Note>?

    suspend fun getNotes(): List<Note>?

    suspend fun refreshNotes()

    suspend fun addNote(note: Note)

    suspend fun updateNote(note: Note)

    suspend fun deleteNote(noteId: String)

    suspend fun deleteAllNotes()
}