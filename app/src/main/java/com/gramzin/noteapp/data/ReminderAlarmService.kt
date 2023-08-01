package com.gramzin.noteapp.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ReminderAlarmService @Inject constructor(
    private val alarmManager: AlarmManager,
    @ApplicationContext
    private val context: Context
) {

    fun createAlarm(alarmId: Long, reminderTimestamp: Long){
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            reminderTimestamp,
            createReminderAlarmIntent(alarmId)
        )
    }

    fun clearAlarm(alarmId: Long){
        alarmManager.cancel(createReminderAlarmIntent(alarmId))
    }

    private fun createReminderAlarmIntent(alarmId: Long): PendingIntent{
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(AlarmReceiver.ALARM_ID_KEY, alarmId)
        }
        return PendingIntent
            .getBroadcast(context, alarmId.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}