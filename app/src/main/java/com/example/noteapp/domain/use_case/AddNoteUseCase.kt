package com.example.noteapp.domain.use_case

import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.repository.NoteRepository
import com.example.noteapp.domain.utils.FieldsNotFieldException
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(note: Note){
        if (note.content.isEmpty() || note.title.isEmpty()) throw FieldsNotFieldException()
        repository.insertNote(note)
    }
}