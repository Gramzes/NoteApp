package com.gramzin.noteapp.domain.use_case

import com.gramzin.noteapp.domain.model.Note
import com.gramzin.noteapp.domain.repository.NoteRepository
import com.gramzin.noteapp.domain.utils.FieldsNotFieldException
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(note: Note){
        if (note.content.isEmpty() || note.title.isEmpty()) throw FieldsNotFieldException()
        repository.insertNote(note)
    }
}