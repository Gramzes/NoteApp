package com.gramzin.noteapp.data.data_storage.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gramzin.noteapp.data.model.NoteEntity
import com.gramzin.noteapp.data.model.ReminderEntity

@Database(entities = [NoteEntity::class, ReminderEntity::class], version = 2)
abstract class NoteDatabase: RoomDatabase() {

    abstract fun noteDao(): NoteDao

    abstract fun reminderDao(): ReminderDao
}