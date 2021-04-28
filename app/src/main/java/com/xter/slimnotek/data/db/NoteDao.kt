package com.xter.slimnotek.data.db

import androidx.room.*
import com.xter.slimnotek.data.entity.Note

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(notes: List<Note>?): List<Long>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg notes: Note?): List<Long>?

    @Query("delete from note where noteid = :noteid")
    fun delete(noteid:String)

    @Delete
    fun delete(note: Note?): Int

    @Delete
    fun delete(notes: List<Note>?): Int

    @Delete
    fun delete(vararg notes: Note?): Int

    @Query("delete from note")
    fun deleteAll()

    @Update
    fun update(note: Note?): Int

    @Update
    fun update(vararg notes: Note?): Int

    @Update
    fun update(notes: List<Note>?): Int

    @Query("select * from note where title == :title")
    fun findByTitle(title: String?): List<Note>?

    @Query("select * from note where noteid = :noteid limit 1")
    fun findById(noteid:String): Note?

    @Query("select * from note ORDER BY updateTime DESC")
    fun findAll(): List<Note>?
}