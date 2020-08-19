package com.xter.slimnotek.data

import androidx.lifecycle.LiveData
import com.xter.slimnotek.util.L

class NoteRemoteSource:NoteSource {
    override fun observeNotes(): LiveData<List<Note>> {
        TODO("Not yet implemented")
    }

    override fun observeNotes(noteid: String): LiveData<Note> {
        TODO("Not yet implemented")
    }

    override suspend fun getNote(noteId: String): Note? {
        TODO("Not yet implemented")
    }

    override suspend fun getNotes(title: String): List<Note>? {
        TODO("Not yet implemented")
    }

    override suspend fun getNotes(): List<Note>? {
        TODO("Not yet implemented")
    }

    override suspend fun refreshNotes() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNotes(notes: List<Note>) {
        TODO("Not yet implemented")
    }

    override suspend fun addNote(note: Note) {
        TODO("Not yet implemented")
    }

    override suspend fun updateNote(note: Note) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNote(noteId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllNotes() {
        TODO("Not yet implemented")
    }
}