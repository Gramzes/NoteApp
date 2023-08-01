package com.gramzin.noteapp.domain.use_case

import com.gramzin.noteapp.domain.repository.ReminderRepository
import javax.inject.Inject

class CheckIsFirstLaunchUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository
) {

    operator fun invoke(): Boolean{
        return reminderRepository.checkIsFirstLaunch()
    }
}