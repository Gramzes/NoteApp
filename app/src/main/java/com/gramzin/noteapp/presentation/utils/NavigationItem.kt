package com.gramzin.noteapp.presentation.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.gramzin.noteapp.R
import com.gramzin.noteapp.navigation.Screen

sealed class NavigationItem(
    val screen: Screen,
    val titleResId: Int,
    val icon: ImageVector
){
    object Notes: NavigationItem(
        Screen.Notes,
        R.string.notes_screen,
        Icons.Default.Home
    )

    object Reminders: NavigationItem(
        Screen.Reminders,
        R.string.reminders_screen,
        Icons.Default.Home
    )
}
