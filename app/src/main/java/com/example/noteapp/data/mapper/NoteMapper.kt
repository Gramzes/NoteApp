package com.example.noteapp.data.mapper


import com.example.noteapp.data.model.NoteEntity
import com.example.noteapp.domain.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NoteMapper @Inject constructor() {

    fun mapNoteEntityToDomain(note: NoteEntity): Note {
        return Note(
            title = note.title,
            content = note.content,
            color = note.color,
            timestamp = note.timestamp,
            id = note.id
        )
    }

    fun mapNoteDomainToData(note: Note): NoteEntity {
        return NoteEntity(
            title = note.title,
            content = note.content,
            color = note.color,
            timestamp = note.timestamp,
            id = note.id
        )
    }

    fun mapNoteEntityFlowToDomain(noteFlow: Flow<NoteEntity?>): Flow<Note?> {
        return noteFlow.map {
            it?.let {
                mapNoteEntityToDomain(it)
            }
        }
    }

    fun mapNoteEntityListFlowToDomain(noteFlow: Flow<List<NoteEntity>>): Flow<List<Note>> {
        return noteFlow.map {
            it.map {
                mapNoteEntityToDomain(it)
            }
        }
    }
}