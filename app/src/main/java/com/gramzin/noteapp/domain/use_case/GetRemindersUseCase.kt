package com.gramzin.noteapp.domain.use_case

import com.gramzin.noteapp.domain.model.Reminder
import com.gramzin.noteapp.domain.repository.ReminderRepository
import com.gramzin.noteapp.domain.utils.OrderType
import com.gramzin.noteapp.domain.utils.ReminderOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRemindersUseCase @Inject constructor(
    private val repository: ReminderRepository
) {

    operator fun invoke(reminderOrder: ReminderOrder): Flow<List<Reminder>> {
        return repository.getReminders().map { notes ->
            when(reminderOrder.orderType){
                is OrderType.Ascending -> {
                    when(reminderOrder){
                        is ReminderOrder.ReminderDate -> notes.sortedBy { it.reminderTimestamp }
                        is ReminderOrder.CreationDate -> notes.sortedBy { it.creationTimestamp }
                        is ReminderOrder.Title -> notes.sortedBy { it.reminder }
                    }
                }
                is OrderType.Descending -> {
                    when(reminderOrder){
                        is ReminderOrder.ReminderDate -> notes
                            .sortedByDescending { it.reminderTimestamp }
                        is ReminderOrder.CreationDate -> notes
                            .sortedByDescending { it.creationTimestamp }
                        is ReminderOrder.Title -> notes
                            .sortedByDescending { it.reminder }
                    }
                }
            }
        }
    }
}