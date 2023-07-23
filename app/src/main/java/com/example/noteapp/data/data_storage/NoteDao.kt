package com.example.noteapp.data.data_storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.noteapp.data.data_storage.DBInfo.NoteTable
import com.example.noteapp.data.model.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM ${DBInfo.NOTE_TABLE_NAME} WHERE ${NoteTable.ID_COLUMN} =:id")
    fun getNoteById(id: Int): Flow<NoteEntity>

    @Query("SELECT * FROM ${DBInfo.NOTE_TABLE_NAME}")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: NoteEntity)

    @Delete
    fun deleteNote(note: NoteEntity)
}