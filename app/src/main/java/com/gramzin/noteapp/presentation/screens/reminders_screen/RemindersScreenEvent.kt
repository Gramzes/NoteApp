package com.gramzin.noteapp.presentation.screens.reminders_screen

import com.gramzin.noteapp.domain.model.Reminder
import com.gramzin.noteapp.domain.utils.ReminderOrder

sealed interface RemindersScreenEvent{

    data class Order(val reminderOrder: ReminderOrder): RemindersScreenEvent

    data class DeleteReminder(val reminder: Reminder): RemindersScreenEvent

    data class OnEditReminderClick(val reminder: Reminder): RemindersScreenEvent

    data class ChangeReminderCompleteState(
        val reminder: Reminder,
        val newCompletedState: Boolean
        ): RemindersScreenEvent

    object ToggleOrderSection: RemindersScreenEvent

    object RestoreReminder: RemindersScreenEvent

    object OpenEditReminderDialog: RemindersScreenEvent

    object OnCloseAutoStartGrantDialog: RemindersScreenEvent
}