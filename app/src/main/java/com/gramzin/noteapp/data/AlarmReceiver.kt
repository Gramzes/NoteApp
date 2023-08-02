package com.gramzin.noteapp.data

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.gramzin.noteapp.R
import com.gramzin.noteapp.domain.model.Reminder
import com.gramzin.noteapp.domain.repository.ReminderRepository
import com.gramzin.noteapp.navigation.Screen
import com.gramzin.noteapp.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.Closeable
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver: BroadcastReceiver(), Closeable {

    @Inject
    lateinit var repository: ReminderRepository
    @Inject
    lateinit var reminderAlarmService: ReminderAlarmService

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onReceive(context: Context?, intent: Intent?) {
        coroutineScope.launch {
            if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
                val reminders = repository.getReminders()
                reminders.first().forEach {
                    if (!it.isCompleted) {
                        reminderAlarmService.createAlarm(it.id!!.toLong(), it.reminderTimestamp)
                    }
                }
            } else {
                coroutineScope.launch {
                    val id = intent?.getLongExtra(ALARM_ID_KEY, -1)
                    val reminder = repository.getReminder(id!!.toInt()).filterNotNull().first()
                    context?.let {
                        createNotification(context, id.toInt(), reminder)
                    }
                    repository.refreshReminderFlow(reminder)
                }
            }
        }
    }

    private fun createNotification(
        context: Context,
        reminderId: Int,
        reminder: Reminder
    ){
        val notificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.alarm_icon)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(reminder.reminder)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(createDeepLink(context))
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(context)){
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(reminderId, notification)
        }
    }

    private fun createDeepLink(context: Context): PendingIntent{
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            "https://com.gramzin.noteapp/${Screen.Reminders.destination}".toUri(),
            context,
            MainActivity::class.java
        )

        return TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(deepLinkIntent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }
    }

    override fun close() {
        coroutineScope.cancel()
    }

    companion object{
        const val CHANNEL_ID = "1241241"
        const val CHANNEL_NAME = "Reminders"
        const val ALARM_ID_KEY = "alarm_id"
    }
}