package com.gramzin.noteapp.presentation.screens.edit_reminder_dialog

import androidx.compose.ui.focus.FocusState

sealed interface EditReminderEvent{

    data class TitleEntered(val text: String): EditReminderEvent

    data class ChangeTitleFocus(val focusState: FocusState): EditReminderEvent

    data class DateTimeSelected(val timeInMillis: Long): EditReminderEvent

    object TimePickerVisibleChange: EditReminderEvent

    object OnCloseDialog: EditReminderEvent

    object SaveReminder: EditReminderEvent
}