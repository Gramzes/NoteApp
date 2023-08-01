package com.gramzin.noteapp.data.repository

import com.gramzin.noteapp.data.data_storage.database.NoteDao
import com.gramzin.noteapp.data.mapper.NoteMapper
import com.gramzin.noteapp.domain.model.Note
import com.gramzin.noteapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
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
        noteDao.insertNote(entityNote)
    }
}