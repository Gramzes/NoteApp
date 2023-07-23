package com.example.noteapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.noteapp.data.data_storage.DBInfo

@Entity(tableName = DBInfo.NOTE_TABLE_NAME)
data class NoteEntity(
    @ColumnInfo(name = DBInfo.NoteTable.TITLE_COLUMN)
    val title: String,
    @ColumnInfo(name = DBInfo.NoteTable.CONTENT_COLUMN)
    val content: String,
    @ColumnInfo(name = DBInfo.NoteTable.COLOR_COLUMN)
    val color: Int,
    @ColumnInfo(name = DBInfo.NoteTable.TIMESTAMP_COLUMN)
    val timestamp: Long,
    @PrimaryKey(true)
    @ColumnInfo(name = DBInfo.NoteTable.ID_COLUMN)
    val id: Int? = null
)
