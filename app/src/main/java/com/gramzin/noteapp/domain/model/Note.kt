package com.gramzin.noteapp.domain.model

import com.gramzin.noteapp.ui.theme.Green
import com.gramzin.noteapp.ui.theme.Orange
import com.gramzin.noteapp.ui.theme.Pantone
import com.gramzin.noteapp.ui.theme.Purple80
import com.gramzin.noteapp.ui.theme.Yellow

data class Note(
    val title: String,
    val content: String,
    val color: Int,
    val timestamp: Long,
    val id: Int? = null
){
    companion object{
        val noteColors = listOf(Purple80, Pantone, Yellow, Orange, Green)
    }
}
