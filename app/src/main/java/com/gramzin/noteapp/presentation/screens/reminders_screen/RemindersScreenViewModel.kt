package com.gramzin.noteapp.presentation.screens.reminders_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramzin.noteapp.domain.model.Reminder
import com.gramzin.noteapp.domain.use_case.AddReminderUseCase
import com.gramzin.noteapp.domain.use_case.CheckIsFirstLaunchUseCase
import com.gramzin.noteapp.domain.use_case.DeleteReminderUseCase
import com.gramzin.noteapp.domain.use_case.GetRemindersUseCase
import com.gramzin.noteapp.domain.utils.OrderType
import com.gramzin.noteapp.domain.utils.ReminderOrder
import com.gramzin.noteapp.presentation.screens.edit_reminder_dialog.EditDialogStateHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemindersScreenViewModel @Inject constructor(
    private val deleteReminderUseCase: DeleteReminderUseCase,
    private val getRemindersUseCase: GetRemindersUseCase,
    private val addReminderUseCase: AddReminderUseCase,
    checkIsFirstLaunchUseCase: CheckIsFirstLaunchUseCase
): ViewModel() {

    private var lastDeletedReminder: Reminder? = null
    private val _state = mutableStateOf(
        RemindersScreenState(
            completedReminders = listOf(),
            uncompletedReminders = listOf(),
            isAutoStartDialogVisible = checkIsFirstLaunchUseCase()
        )
    )
    val state: State<RemindersScreenState> = _state

    private val _editDialogStateHandler = mutableStateOf<EditDialogStateHandler?>(null)
    val editDialogStateHandler: State<EditDialogStateHandler?> = _editDialogStateHandler

    private var flowJob: Job = Job()

    init {
        getReminders(ReminderOrder.ReminderDate(OrderType.Descending))
    }

    fun onEvent(event: RemindersScreenEvent){
        when(event){
            is RemindersScreenEvent.DeleteReminder -> {
                viewModelScope.launch {
                    Log.d("TEST delete", _state.value.reminderOrder.toString() + " " + _state.value.reminderOrder.orderType.toString())
                    lastDeletedReminder = event.reminder
                    deleteReminderUseCase(event.reminder)
                }
            }
            is RemindersScreenEvent.Order -> {
                Log.d("TEST order", _state.value.reminderOrder.toString() + " " + _state.value.reminderOrder.orderType.toString())
                if (_state.value.reminderOrder != event.reminderOrder){
                    getReminders(event.reminderOrder)
                }
            }
            RemindersScreenEvent.RestoreReminder -> {
                Log.d("TEST restore", _state.value.reminderOrder.toString() + " " + _state.value.reminderOrder.orderType.toString())
                viewModelScope.launch {
                    lastDeletedReminder?.let {
                        addReminderUseCase(it)
                        lastDeletedReminder = null
                    }
                }
            }
            RemindersScreenEvent.ToggleOrderSection -> {
                Log.d("TEST toggle section", _state.value.reminderOrder.toString() + " " + _state.value.reminderOrder.orderType.toString())
                _state.value = _state.value.copy(
                    isOrderSelectionVisible = !_state.value.isOrderSelectionVisible
                )
            }
            is RemindersScreenEvent.ChangeReminderCompleteState -> {
                Log.d("TEST change complete", _state.value.reminderOrder.toString() + " " + _state.value.reminderOrder.orderType.toString())
                viewModelScope.launch {
                    val newReminderState = event.reminder.copy(
                        isCompleted = event.newCompletedState
                    )
                    addReminderUseCase(newReminderState)
                }
            }

            RemindersScreenEvent.OpenEditReminderDialog -> {
                Log.d("TEST open edit", _state.value.reminderOrder.toString() + " " + _state.value.reminderOrder.orderType.toString())
                _editDialogStateHandler.value = EditDialogStateHandler(
                    addReminderUseCase = addReminderUseCase,
                    onDismissDialog = {
                        _editDialogStateHandler.value = null
                    }
                )
            }

            is RemindersScreenEvent.OnEditReminderClick -> {
                _editDialogStateHandler.value = EditDialogStateHandler(
                    reminder = event.reminder,
                    onDismissDialog = {
                        _editDialogStateHandler.value = null
                    },
                    addReminderUseCase = addReminderUseCase
                )
            }

            RemindersScreenEvent.OnCloseAutoStartGrantDialog -> {
                _state.value = _state.value.copy(isAutoStartDialogVisible = false)
            }
        }
    }

    private fun getReminders(reminderOrder: ReminderOrder){
        flowJob.cancel()
        flowJob = getRemindersUseCase(reminderOrder)
            .onEach {
                _state.value = _state.value.copy(
                    completedReminders = it.filter { it.isCompleted  },
                    uncompletedReminders = it.filter { !it.isCompleted },
                    reminderOrder = reminderOrder
                )
            }
            .launchIn(viewModelScope)
    }
}