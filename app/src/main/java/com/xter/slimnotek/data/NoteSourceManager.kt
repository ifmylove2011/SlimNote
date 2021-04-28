package com.xter.slimnotek.data

import androidx.lifecycle.LiveData
import com.xter.slimnotek.data.entity.Note
import com.xter.slimnotek.util.L
import com.xter.slimnotek.util.Utils
import kotlinx.coroutines.*
import java.util.*

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
        try {
            noteRemoteSource.refreshNotes()
        } catch (e: NotImplementedError) {
            addNote(generateFakeNote())
            observeNotes()
        }
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
                noteLocalSource.refreshNotes()
            }
        }
    }

    override suspend fun deleteNote(noteId: String) {
        coroutineScope {
            launch {
                noteLocalSource.deleteNote(noteId)
                noteLocalSource.refreshNotes()
                L.i("remove: id=$noteId")
            }
        }
    }

    override suspend fun deleteNotes(notes: List<Note>) {
        coroutineScope {
            launch {
                noteLocalSource.deleteNotes(notes)
                noteLocalSource.refreshNotes()
            }
        }
    }

    override suspend fun deleteAllNotes() {
        withContext(ioDispatcher) {
            coroutineScope {
                launch { noteLocalSource.deleteAllNotes() }
            }
        }
    }

    /* -------------------------- 模拟一下 --------------------------- */

    private fun generateFakeNote(): Note {
        val firstDate = Utils.getNormalDate1()
        val content = generateString(Random().nextInt(200) + 50)
        val note = Note(
            generateString(Random().nextInt(12) + 3),
            content,
            content.substring(0, 20),
            firstDate,
            firstDate,
            firstDate
        )
        return note
    }

    fun generateString(size: Int): String {
        val str = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return str.run {
            (0 until size).map {
                this.get(Random().nextInt(str.size))
            }.joinToString("")
        }
    }


}