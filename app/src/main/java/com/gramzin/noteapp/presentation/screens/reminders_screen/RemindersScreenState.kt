package com.gramzin.noteapp.presentation.screens.reminders_screen

import com.gramzin.noteapp.domain.model.Reminder
import com.gramzin.noteapp.domain.utils.OrderType
import com.gramzin.noteapp.domain.utils.ReminderOrder

data class RemindersScreenState(
    val completedReminders: List<Reminder>,
    val uncompletedReminders: List<Reminder>,
    val reminderOrder: ReminderOrder = ReminderOrder.ReminderDate(OrderType.Descending),
    val isOrderSelectionVisible: Boolean = false,
    val isAutoStartDialogVisible: Boolean
)