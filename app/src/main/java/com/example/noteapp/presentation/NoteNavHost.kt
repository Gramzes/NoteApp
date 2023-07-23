package com.example.noteapp.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.noteapp.domain.model.Note
import com.example.noteapp.presentation.screens.notes_screen.NotesScreen
import com.example.noteapp.presentation.utils.Screen

@Composable
fun NoteNavHost(
    navController: NavHostController = rememberNavController(),
    onNavigateToEditNoteScreen: (Note?) -> Unit
) {
    NavHost(navController = navController, startDestination = Screen.Notes.destination){
        composable(route = Screen.Notes.destination){
            NotesScreen(){
                onNavigateToEditNoteScreen(it)
            }
        }
    }
}