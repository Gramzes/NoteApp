package com.gramzin.noteapp.data.data_storage.database

object DBInfo {

    const val NOTE_TABLE_NAME = "notes"
    const val REMINDER_TABLE_NAME = "reminders"

    object NoteTable{
        const val ID_COLUMN = "id"
        const val TITLE_COLUMN = "column"
        const val CONTENT_COLUMN = "content"
        const val COLOR_COLUMN = "color"
        const val TIMESTAMP_COLUMN = "timestamp"
    }

    object ReminderTable{
        const val ID_COLUMN = "id"
        const val REMINDER_COLUMN = "reminder"
        const val COMPLETED_COLUMN = "completed"
        const val CREATION_TIME_COLUMN = "creation_time"
        const val REMINDER_TIME_COLUMN = "reminder_time"
    }
}