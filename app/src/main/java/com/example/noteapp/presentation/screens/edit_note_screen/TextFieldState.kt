package com.example.noteapp.presentation.screens.edit_note_screen

data class TextFieldState(
    val hint: String,
    val text: String = "",
    val isHintVisible: Boolean = true,
)