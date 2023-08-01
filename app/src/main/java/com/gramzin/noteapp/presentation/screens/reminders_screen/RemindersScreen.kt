package com.gramzin.noteapp.presentation.screens.reminders_screen

import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.gramzin.noteapp.R
import com.gramzin.noteapp.presentation.screens.edit_reminder_dialog.EditReminderDialog
import com.gramzin.noteapp.presentation.screens.reminders_screen.components.ReminderItem
import com.gramzin.noteapp.presentation.screens.reminders_screen.components.ReminderOrderSection
import com.judemanutd.autostarter.AutoStartPermissionHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RemindersScreen(
    viewModel: RemindersScreenViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val state = viewModel.state.value

    val snackBarState = remember {
        SnackbarHostState()
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        snackbarHost = {
            SnackbarHost(hostState = snackBarState) {
                Snackbar(
                    modifier = Modifier.padding(10.dp),
                    action = {
                        TextButton(onClick = { it.performAction() }) {
                            Text(stringResource(R.string.yes_answer))
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    Text(
                        text = stringResource(R.string.do_you_want_to_restore_a_note),
                        color = MaterialTheme.colorScheme.inverseOnSurface
                    )
                }
            }
        },
        floatingActionButton = {
            val context = LocalContext.current
            val toastText = stringResource(R.string.permission_denied_text)
            AddReminderActionButton(viewModel = viewModel) {
                Toast.makeText(
                    context,
                    toastText,
                    Toast.LENGTH_LONG
                ).show()
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
                        text = stringResource(R.string.your_reminders_title),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    IconButton(
                        onClick = {
                            viewModel.onEvent(RemindersScreenEvent.ToggleOrderSection)
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
                    exit = slideOutVertically() + fadeOut(),
                ) {
                    ReminderOrderSection(
                        reminderOrder = state.reminderOrder,
                        onOrderChange = { viewModel.onEvent(RemindersScreenEvent.Order(it)) },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.uncompletedReminders, key = {it.id!!}) { reminder ->
                        ReminderItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                                .animateItemPlacement(),
                            reminder = reminder,
                            cornerSize = 10.dp,
                            onDeleteReminder = {
                                viewModel
                                    .onEvent(RemindersScreenEvent.DeleteReminder(reminder))

                                coroutineScope.launch {
                                    when (snackBarState.showSnackbar("")) {
                                        SnackbarResult.Dismissed -> {}
                                        SnackbarResult.ActionPerformed -> {
                                            viewModel.onEvent(RemindersScreenEvent.RestoreReminder)
                                        }
                                    }
                                }
                            },
                            onItemClick = {
                                viewModel
                                    .onEvent(RemindersScreenEvent.OnEditReminderClick(reminder))
                            },
                            onCompletedChange = {
                                viewModel.onEvent(
                                    RemindersScreenEvent.ChangeReminderCompleteState(
                                        reminder,
                                        it
                                    )
                                )
                            }
                        )
                    }
                    items(state.completedReminders, key = {it.id!!}) { reminder ->
                        ReminderItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                                .animateItemPlacement(),
                            reminder = reminder,
                            cornerSize = 10.dp,
                            onDeleteReminder = {
                                viewModel
                                    .onEvent(RemindersScreenEvent.DeleteReminder(reminder))
                                coroutineScope.launch {
                                    when (snackBarState.showSnackbar("")) {
                                        SnackbarResult.Dismissed -> {}
                                        SnackbarResult.ActionPerformed -> {
                                            viewModel.onEvent(RemindersScreenEvent.RestoreReminder)
                                        }
                                    }
                                }
                            },
                            onItemClick = {
                                viewModel
                                    .onEvent(RemindersScreenEvent.OnEditReminderClick(reminder))
                            },
                            onCompletedChange = {
                                viewModel.onEvent(
                                    RemindersScreenEvent.ChangeReminderCompleteState(
                                        reminder,
                                        it
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    viewModel.editDialogStateHandler.value?.let {
        EditReminderDialog(it)
    }
    
    if (viewModel.state.value.isAutoStartDialogVisible) {
        AlertDialog(
            onDismissRequest = {
                viewModel.onEvent(RemindersScreenEvent.OnCloseAutoStartGrantDialog)
            },
            confirmButton = {
                val context = LocalContext.current
                TextButton(
                    onClick = {
                        AutoStartPermissionHelper.getInstance().getAutoStartPermission(context)
                        viewModel.onEvent(RemindersScreenEvent.OnCloseAutoStartGrantDialog)
                }) {
                    Text(
                        text = stringResource(R.string.provide),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            dismissButton = {},
            title = { 
                Text(
                    text = stringResource(R.string.permission_required),
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.autostart_dialog_text),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        )
    }
}

@Composable
fun AddReminderActionButton(
    viewModel: RemindersScreenViewModel,
    onDeniedPermission: () -> Unit
){
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            if(it){
                viewModel.onEvent(RemindersScreenEvent.OpenEditReminderDialog)
            } else {
                onDeniedPermission()
            }
        }
    )
    val context = LocalContext.current
    FloatingActionButton(onClick = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val result = ContextCompat
                .checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
            if (result == PackageManager.PERMISSION_GRANTED){
                viewModel.onEvent(RemindersScreenEvent.OpenEditReminderDialog)
            } else{
                launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            viewModel.onEvent(RemindersScreenEvent.OpenEditReminderDialog)
        }
    }) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.add_note_button_text),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}