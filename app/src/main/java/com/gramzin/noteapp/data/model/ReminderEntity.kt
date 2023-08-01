package com.gramzin.noteapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gramzin.noteapp.data.data_storage.database.DBInfo

@Entity(tableName = DBInfo.REMINDER_TABLE_NAME)
data class ReminderEntity(
    @ColumnInfo(DBInfo.ReminderTable.REMINDER_COLUMN)
    val reminder: String,
    @ColumnInfo(DBInfo.ReminderTable.COMPLETED_COLUMN)
    val isCompleted: Boolean,
    @ColumnInfo(DBInfo.ReminderTable.CREATION_TIME_COLUMN)
    val creationTimestamp: Long,
    @ColumnInfo(DBInfo.ReminderTable.REMINDER_TIME_COLUMN)
    val reminderTimestamp: Long,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(DBInfo.ReminderTable.ID_COLUMN)
    val id: Int? = null
)