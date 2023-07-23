package com.example.noteapp.presentation.utils

sealed class Screen(val destination: String){
    object Notes: Screen("notes")
    object EditNote: Screen("edit_notes?noteId={noteId}")
}
