package com.gramzin.noteapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.gramzin.noteapp.domain.model.Note
import com.gramzin.noteapp.presentation.screens.edit_note_screen.EditNoteScreen
import com.gramzin.noteapp.presentation.screens.notes_screen.NotesScreen
import com.gramzin.noteapp.presentation.screens.reminders_screen.RemindersScreen

@Composable
fun NoteNavHost(
    navController: NavHostController = rememberNavController(),
    onNavigateToEditNoteScreen: (Note?) -> Unit,
    onNavigateBackFromEditScreen: () -> Unit
) {
    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = Screen.Notes.destination
    ){
        composable(route = Screen.Notes.destination){
            NotesScreen(viewModel = hiltViewModel()){
                onNavigateToEditNoteScreen(it)
            }
        }
        composable(
            route = Screen.EditNote.destination,
            arguments = listOf(
                navArgument(Screen.EditNote.NOTE_ID_KEY){
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ){
            EditNoteScreen(viewModel = hiltViewModel()){
                onNavigateBackFromEditScreen()
            }
        }
        composable(
            route = Screen.Reminders.destination,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "https://com.gramzin.noteapp/${Screen.Reminders.destination}"
                }
            )
        ){
            RemindersScreen()
        }
    }
}