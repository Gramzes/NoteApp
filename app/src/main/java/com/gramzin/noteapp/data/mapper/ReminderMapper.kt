package com.gramzin.noteapp.data.mapper

import com.gramzin.noteapp.data.model.ReminderEntity
import com.gramzin.noteapp.domain.model.Reminder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReminderMapper @Inject constructor() {

    fun mapReminderEntityToDomain(reminder: ReminderEntity): Reminder {
        return Reminder(
            reminder.reminder,
            reminder.isCompleted,
            reminder.reminderTimestamp <= System.currentTimeMillis(),
            reminder.creationTimestamp,
            reminder.reminderTimestamp,
            reminder.id
        )
    }

    fun mapReminderDomainToData(reminder: Reminder): ReminderEntity {
        return ReminderEntity(
            reminder.reminder,
            reminder.isCompleted,
            reminder.creationTimestamp,
            reminder.reminderTimestamp,
            reminder.id
        )
    }

    fun mapReminderEntityFlowToDomain(reminderFlow: Flow<ReminderEntity?>): Flow<Reminder?> {
        return reminderFlow.map {
            it?.let {
                mapReminderEntityToDomain(it)
            }
        }
    }

    fun mapReminderEntityListFlowToDomain(
        reminderFlow: Flow<List<ReminderEntity>>
    ): Flow<List<Reminder>> {

        return reminderFlow.map {
            it.map {
                mapReminderEntityToDomain(it)
            }
        }
    }
}