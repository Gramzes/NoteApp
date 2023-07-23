package com.example.noteapp.data.repository

import com.example.noteapp.data.data_storage.NoteDao
import com.example.noteapp.data.mapper.NoteMapper
import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import java.lang.Exception
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val mapper: NoteMapper
): NoteRepository {

    override fun getNotes(): Flow<List<Note>> {
        val entityNotes = noteDao.getAllNotes()
        return mapper.mapNoteEntityListFlowToDomain(entityNotes)
    }

    override fun getNote(id: Int): Flow<Note?> {
        val entityNote = noteDao.getNoteById(id)
        return mapper.mapNoteEntityFlowToDomain(entityNote)
    }

    override suspend fun deleteNote(note: Note) {
        val entityNote = mapper.mapNoteDomainToData(note)
        noteDao.deleteNote(entityNote)
    }

    override suspend fun insertNote(note: Note) {
        val entityNote = mapper.mapNoteDomainToData(note)
        try {
            noteDao.insertNote(entityNote)
        } catch (ex: Exception){
            ex
            val a = 1
        }

    }
}