package com.example.noteapp.presentation.screens.notes_screen

import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.utils.NoteOrder
import com.example.noteapp.domain.utils.OrderType

data class NotesScreenState(
    val notes: List<Note>,
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSelectionVisible: Boolean = false
)