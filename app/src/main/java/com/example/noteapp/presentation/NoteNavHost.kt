package com.example.noteapp.presentation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.noteapp.domain.model.Note
import com.example.noteapp.presentation.screens.edit_note_screen.EditNoteScreen
import com.example.noteapp.presentation.screens.edit_note_screen.EditNoteViewModel
import com.example.noteapp.presentation.screens.notes_screen.NotesScreen
import com.example.noteapp.presentation.utils.Screen

@Composable
fun NoteNavHost(
    navController: NavHostController = rememberNavController(),
    onNavigateToEditNoteScreen: (Note?) -> Unit,
    onNavigateBackFromEditScreen: () -> Unit
) {
    NavHost(navController = navController, startDestination = Screen.Notes.destination){
        composable(route = Screen.Notes.destination){
            Log.d("TEST", "composable")
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
    }
}