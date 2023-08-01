package com.gramzin.noteapp.data.data_storage.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gramzin.noteapp.data.data_storage.database.DBInfo.NoteTable
import com.gramzin.noteapp.data.model.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM ${DBInfo.NOTE_TABLE_NAME} WHERE ${NoteTable.ID_COLUMN} =:id")
    fun getNoteById(id: Int): Flow<NoteEntity>

    @Query("SELECT * FROM ${DBInfo.NOTE_TABLE_NAME}")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)
}