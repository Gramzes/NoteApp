package com.example.noteapp.domain.repository

import com.example.noteapp.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getNotes(): Flow<List<Note>>

    fun getNote(id: Int): Flow<Note?>

    suspend fun deleteNote(note: Note)

    suspend fun insertNote(note: Note)
}