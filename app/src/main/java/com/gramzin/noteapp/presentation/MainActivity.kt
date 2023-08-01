package com.gramzin.noteapp.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.gramzin.noteapp.navigation.NoteNavHost
import com.gramzin.noteapp.presentation.components.TabsContainer
import com.gramzin.noteapp.navigation.Screen
import com.gramzin.noteapp.ui.theme.NoteAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteAppTheme {
                val navController = rememberNavController()
                Scaffold(
                    topBar = {
                        TabsContainer(
                            navController = navController,
                            onItemClick = {
                                navController.navigate(it.screen.destination){
                                    launchSingleTop = true
                                    popUpTo(navController.graph.findStartDestination().id){
                                        saveState = true
                                    }
                                    restoreState = true
                                }
                            }
                        )
                    }
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
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
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }
}