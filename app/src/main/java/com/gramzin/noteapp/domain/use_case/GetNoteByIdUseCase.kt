package com.gramzin.noteapp.domain.use_case

import com.gramzin.noteapp.domain.model.Note
import com.gramzin.noteapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(
    private val repository: NoteRepository
) {

    operator fun invoke(id: Int): Flow<Note?> {
        return repository.getNote(id)
    }
}