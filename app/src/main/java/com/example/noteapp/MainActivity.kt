package com.example.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.noteapp.presentation.NoteNavHost
import com.example.noteapp.presentation.screens.notes_screen.NotesScreen
import com.example.noteapp.presentation.utils.Screen
import com.example.noteapp.ui.theme.NoteAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteAppTheme {
                val navController = rememberNavController()
                NoteNavHost(
                    navController = navController,
                    onNavigateToEditNoteScreen = {
                        navController.navigate(
                            Screen.EditNote.getRouteWithArgs(it?.id)
                        )
                    },
                    onNavigateBackFromEditScreen = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}