package com.example.noteapp.presentation.screens.notes_screen

import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.utils.NoteOrder

sealed interface NotesScreenEvent{

    data class Order(val noteOrder: NoteOrder): NotesScreenEvent

    data class DeleteNote(val note: Note): NotesScreenEvent

    object ToggleOrderSection: NotesScreenEvent

    object RestoreNote: NotesScreenEvent


}