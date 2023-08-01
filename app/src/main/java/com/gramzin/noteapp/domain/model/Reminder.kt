package com.gramzin.noteapp.domain.model

data class Reminder(
    val reminder: String,
    val isCompleted: Boolean,
    val isExpired: Boolean,
    val creationTimestamp: Long,
    val reminderTimestamp: Long,
    val id: Int? = null
)
