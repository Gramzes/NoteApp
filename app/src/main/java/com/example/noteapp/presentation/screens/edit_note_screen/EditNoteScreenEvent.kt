package com.example.noteapp.presentation.screens.edit_note_screen

import androidx.compose.ui.focus.FocusState

sealed class EditNoteScreenEvent{

    data class TitleEntered(val text: String): EditNoteScreenEvent()

    data class ContentEntered(val text: String): EditNoteScreenEvent()

    data class ColorChanged(val color: Int): EditNoteScreenEvent()

    data class ChangeTitleFocus(val focusState: FocusState): EditNoteScreenEvent()

    data class ChangeContentFocus(val focusState: FocusState): EditNoteScreenEvent()

    object SaveNote: EditNoteScreenEvent()

}
