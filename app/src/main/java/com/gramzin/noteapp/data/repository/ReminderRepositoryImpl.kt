package com.gramzin.noteapp.data.repository

import com.gramzin.noteapp.data.ReminderAlarmService
import com.gramzin.noteapp.data.data_storage.FirstTimeLaunchAppStorage
import com.gramzin.noteapp.data.data_storage.database.ReminderDao
import com.gramzin.noteapp.data.mapper.ReminderMapper
import com.gramzin.noteapp.domain.model.Reminder
import com.gramzin.noteapp.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(
    private val reminderDao: ReminderDao,
    private val mapper: ReminderMapper,
    private val reminderAlarmService: ReminderAlarmService,
    private val firstTimeLaunchAppStorage: FirstTimeLaunchAppStorage
): ReminderRepository {

    override fun getReminders(): Flow<List<Reminder>> {
        val entityReminders = reminderDao.getAllReminders()
        return mapper.mapReminderEntityListFlowToDomain(entityReminders)
    }

    override fun getReminder(id: Int): Flow<Reminder?> {
        val entityReminder = reminderDao.getReminderById(id)
        return mapper.mapReminderEntityFlowToDomain(entityReminder)
    }

    override suspend fun deleteReminder(reminder: Reminder) {
        val entityReminder = mapper.mapReminderDomainToData(reminder)
        reminderDao.deleteReminder(entityReminder)
    }

    override suspend fun insertReminder(reminder: Reminder) {
        val entityReminder = mapper.mapReminderDomainToData(reminder)
        val id = reminderDao.insertReminder(entityReminder)

        reminderAlarmService.clearAlarm(id)
        if (!entityReminder.isCompleted) {
            reminderAlarmService.createAlarm(id, entityReminder.reminderTimestamp)
        }
    }

    override suspend fun refreshReminderFlow(reminder: Reminder){
        val entityReminder = mapper.mapReminderDomainToData(reminder)
        reminderDao.insertReminder(entityReminder)
    }

    override fun checkIsFirstLaunch(): Boolean{
        return firstTimeLaunchAppStorage.checkIsFirstLaunch()
    }
}