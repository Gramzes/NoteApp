package com.example.noteapp.presentation.screens.notes_screen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.noteapp.R
import com.example.noteapp.domain.model.Note
import com.example.noteapp.presentation.screens.notes_screen.components.NoteItem
import com.example.noteapp.presentation.screens.notes_screen.components.OrderSection
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    viewModel: NotesScreenViewModel,
    onNavigateToEditScreenClick: (Note?) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val state = viewModel.state.value
    val snackBarState = remember {
        SnackbarHostState()
    }
    Log.d("TEST", "NotesScreen")
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackBarState){
                Snackbar(
                    modifier = Modifier.padding(10.dp),
                    action = {
                        TextButton(onClick = { it.performAction() }) {
                            Text(stringResource(R.string.yes_answer))
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.surface
                ){
                    Text(
                        text = stringResource(R.string.restore_note_message),
                        color = MaterialTheme.colorScheme.inverseOnSurface
                    )
                }
            } },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onNavigateToEditScreenClick(null)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_note_button_text),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(R.string.your_note_title),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    IconButton(
                        onClick = {
                            viewModel.onEvent(NotesScreenEvent.ToggleOrderSection)
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_sort_24),
                            contentDescription = stringResource(R.string.sort_notes_button),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
                AnimatedVisibility(
                    visible = state.isOrderSelectionVisible,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    OrderSection(
                        noteOrder = state.noteOrder,
                        onOrderChange = { viewModel.onEvent(NotesScreenEvent.Order(it)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                val snackBarText = stringResource(R.string.restore_note_message)
                val actionText = stringResource(R.string.yes_answer)
                LazyColumn {
                    items(state.notes){
                        NoteItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            note = it,
                            cutCornerSize = 30.dp,
                            cornerRadius = 10.dp,
                            onDeleteNoteClick = {
                                viewModel.onEvent(NotesScreenEvent.DeleteNote(it))
                                coroutineScope.launch {
                                    when(snackBarState.showSnackbar("")){
                                        SnackbarResult.Dismissed -> {}
                                        SnackbarResult.ActionPerformed -> {
                                            viewModel.onEvent(NotesScreenEvent.RestoreNote)
                                        }
                                    }
                                }
                            },
                            onNoteClick = {
                                onNavigateToEditScreenClick(it)
                            }
                        )
                    }
                }
            }
        }
    }
}