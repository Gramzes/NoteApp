package com.gramzin.noteapp.domain.use_case

import com.gramzin.noteapp.domain.model.Reminder
import com.gramzin.noteapp.domain.repository.ReminderRepository
import javax.inject.Inject

class DeleteReminderUseCase @Inject constructor(
    private val repository: ReminderRepository
) {

    suspend operator fun invoke(reminder: Reminder){
        repository.deleteReminder(reminder)
    }
}