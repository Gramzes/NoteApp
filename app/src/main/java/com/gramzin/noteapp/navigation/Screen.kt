package com.gramzin.noteapp.navigation

sealed class Screen(val destination: String){
    object Notes: Screen("notes")

    object EditNote: Screen("edit_notes?note_id={note_id}"){

        const val NOTE_ID_KEY = "note_id"
        fun getRouteWithArgs(noteId: Int?): String {
            return if(noteId != null) "edit_notes?$NOTE_ID_KEY=$noteId" else "edit_notes"
        }
    }

    object Reminders: Screen("reminders")
}
