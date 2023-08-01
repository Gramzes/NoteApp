package com.gramzin.noteapp.domain.use_case

import com.gramzin.noteapp.domain.model.Reminder
import com.gramzin.noteapp.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetReminderByIdUseCase @Inject constructor(
    private val repository: ReminderRepository
) {

    operator fun invoke(id: Int): Flow<Reminder?> {
        return repository.getReminder(id)
    }
}