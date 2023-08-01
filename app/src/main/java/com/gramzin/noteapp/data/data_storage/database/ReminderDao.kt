package com.gramzin.noteapp.data.data_storage.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gramzin.noteapp.data.model.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Query("SELECT * FROM ${DBInfo.REMINDER_TABLE_NAME} WHERE ${DBInfo.ReminderTable.ID_COLUMN} =:id")
    fun getReminderById(id: Int): Flow<ReminderEntity>

    @Query("SELECT * FROM ${DBInfo.REMINDER_TABLE_NAME}")
    fun getAllReminders(): Flow<List<ReminderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderEntity): Long

    @Delete
    suspend fun deleteReminder(reminder: ReminderEntity)
}