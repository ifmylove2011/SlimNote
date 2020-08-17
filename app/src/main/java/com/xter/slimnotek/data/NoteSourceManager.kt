package com.xter.slimnotek.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.*

class NoteSourceManager(
    private val noteRemoteSource: NoteSource,
    private val noteLocalSource: NoteSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : NoteSource {
    override fun observeNotes(): LiveData<List<Note>> {
        return noteLocalSource.observeNotes()
    }

    override fun observeNotes(noteid: String): LiveData<Note> {
        return noteLocalSource.observeNotes(noteid)
    }

    override suspend fun getNote(noteId: String): Note? {
        return noteLocalSource.getNote(noteId)
    }

    override suspend fun getNotes(title: String): List<Note>? {
        return noteLocalSource.getNotes(title)
    }

    override suspend fun getNotes(): List<Note>? {
        return noteLocalSource.getNotes()
    }

    override suspend fun refreshNotes() {
        noteLocalSource.refreshNotes()
    }

    override suspend fun addNote(note: Note) {
        coroutineScope {
            launch {
                noteLocalSource.addNote(note)
            }
        }
    }

    override suspend fun updateNote(note: Note) {
        coroutineScope {
            launch {
                noteLocalSource.updateNote(note)
            }
        }
    }

    override suspend fun deleteNote(noteId: String) {
        coroutineScope {
            launch { noteLocalSource.deleteNote(noteId) }
        }
    }

    override suspend fun deleteAllNotes() {
        withContext(ioDispatcher) {
            coroutineScope {
                launch { noteLocalSource.deleteAllNotes() }
            }
        }
    }

}