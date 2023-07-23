package com.example.noteapp.data.data_storage

import androidx.compose.ui.graphics.Color
import androidx.room.PrimaryKey

object DBInfo {

    const val NOTE_TABLE_NAME = "notes"

    object NoteTable{
        const val ID_COLUMN = "id"
        const val TITLE_COLUMN = "column"
        const val CONTENT_COLUMN = "content"
        const val COLOR_COLUMN = "color"
        const val TIMESTAMP_COLUMN = "timestamp"
    }
}