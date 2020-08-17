package com.xter.slimnotek.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.xter.slimnotek.util.L
import com.xter.slimnotek.data.db.NoteDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class NoteLocalSource constructor(
    private val noteDao: NoteDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : NoteSource {

    private val observableNotes = MutableLiveData<List<Note>>()

    override fun observeNotes(): LiveData<List<Note>> {
        runBlocking {
            L.d("Thread is ${Thread.currentThread().name}")
            refreshNotes()
        }
        return observableNotes
    }

    override fun observeNotes(noteid: String): LiveData<Note> {
        TODO("Not yet implemented")
    }

    override suspend fun getNote(noteId: String): Note? = withContext(ioDispatcher) {
        return@withContext noteDao.findById(noteId)
    }

    override suspend fun getNotes(title: String): List<Note>? = withContext(ioDispatcher) {
        return@withContext noteDao.findByTitle(title)
    }

    override suspend fun getNotes(): List<Note>? = withContext(ioDispatcher) {
        L.d("Thread is ${Thread.currentThread().name}")
        return@withContext noteDao.findAll()
    }

    override suspend fun refreshNotes() {
        observableNotes.value = getNotes()
    }

    override suspend fun addNote(note: Note) {
        withContext(ioDispatcher) {
            noteDao.insert(note)
        }
    }

    override suspend fun updateNote(note: Note) {
        withContext(ioDispatcher) {
            noteDao.insert(note)
        }
    }

    override suspend fun deleteNote(noteId: String) {
        withContext(ioDispatcher) {
            noteDao.delete(noteId)
        }
    }

    override suspend fun deleteAllNotes() {
        withContext(ioDispatcher) {
            noteDao.deleteAll()
        }
    }
}