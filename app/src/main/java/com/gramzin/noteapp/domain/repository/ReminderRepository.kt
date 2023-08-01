package com.gramzin.noteapp.domain.repository

import com.gramzin.noteapp.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {

    fun getReminders(): Flow<List<Reminder>>

    fun getReminder(id: Int): Flow<Reminder?>

    suspend fun deleteReminder(reminder: Reminder)

    suspend fun insertReminder(reminder: Reminder)

    fun checkIsFirstLaunch(): Boolean

    suspend fun refreshReminderFlow(reminder: Reminder)
}