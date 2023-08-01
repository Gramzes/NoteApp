package com.gramzin.noteapp.presentation.screens.notes_screen

import com.gramzin.noteapp.domain.model.Note
import com.gramzin.noteapp.domain.utils.NoteOrder
import com.gramzin.noteapp.domain.utils.OrderType

data class NotesScreenState(
    val notes: List<Note>,
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSelectionVisible: Boolean = false
)