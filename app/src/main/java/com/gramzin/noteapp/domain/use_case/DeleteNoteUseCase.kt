package com.gramzin.noteapp.domain.use_case

import com.gramzin.noteapp.domain.model.Note
import com.gramzin.noteapp.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(note: Note){
        repository.deleteNote(note)
    }
}