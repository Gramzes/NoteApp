package com.gramzin.noteapp.presentation.screens.edit_reminder_dialog

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusState
import com.gramzin.noteapp.domain.model.Reminder
import com.gramzin.noteapp.domain.use_case.AddReminderUseCase
import com.gramzin.noteapp.presentation.utils.TextFieldState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.Closeable

class EditDialogStateHandler(
    private val onDismissDialog: () -> Unit,
    private val addReminderUseCase: AddReminderUseCase,
    private val reminder: Reminder? = null,
): Closeable {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val _isTimePickerDialogVisible = mutableStateOf(false)
    val isTimePickerDialogVisible: State<Boolean> = _isTimePickerDialogVisible

    private val _selectedAlarmTime = MutableStateFlow<Long?>(null)
    val selectedAlarmTime = _selectedAlarmTime.asStateFlow()

    private val _titleState = MutableStateFlow(TextFieldState())
    val titleState = _titleState.asStateFlow()

    val isButtonEnabled = combine(titleState, selectedAlarmTime){ titleState, selectedTime ->
        (titleState.text.isNotEmpty() && selectedTime != null)
    }.stateIn(coroutineScope, SharingStarted.Lazily, false)

    init {
        reminder?.let {
            _selectedAlarmTime.value = reminder.reminderTimestamp
            _titleState.value = TextFieldState(text = reminder.reminder, isHintVisible = false)
        }
    }

    fun onEvent(event: EditReminderEvent) {
        when(event){
            is EditReminderEvent.ChangeTitleFocus -> {
                _titleState.value = _titleState.value.copy(
                    isHintVisible = checkIsHintVisible(_titleState.value.text, event.focusState)
                )
            }
            is EditReminderEvent.TitleEntered -> {
                _titleState.value = _titleState.value.copy(
                    text = event.text
                )
            }
            is EditReminderEvent.DateTimeSelected -> {
                _isTimePickerDialogVisible.value = false
                _selectedAlarmTime.value = event.timeInMillis
            }
            EditReminderEvent.SaveReminder -> {
                val selectedTime = selectedAlarmTime.value ?: return
                val newReminder = Reminder(
                    id = reminder?.id,
                    reminder = titleState.value.text,
                    isCompleted = reminder?.isCompleted ?: false,
                    isExpired = System.currentTimeMillis() > selectedTime,
                    creationTimestamp = System.currentTimeMillis(),
                    reminderTimestamp = selectedTime
                )
                coroutineScope.launch(Dispatchers.IO) {
                    addReminderUseCase(newReminder)
                    onEvent(EditReminderEvent.OnCloseDialog)
                }
            }

            EditReminderEvent.OnCloseDialog -> { onDismissDialog() }
            EditReminderEvent.TimePickerVisibleChange -> {
                _isTimePickerDialogVisible.value = !_isTimePickerDialogVisible.value
            }
        }
    }

    private fun checkIsHintVisible(text: String, focus: FocusState): Boolean{
        return !(focus.isFocused || text.isNotEmpty())
    }

    override fun close() {
        coroutineScope.cancel()
    }
}