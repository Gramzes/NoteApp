package com.gramzin.noteapp.presentation.screens.edit_note_screen

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gramzin.noteapp.R
import com.gramzin.noteapp.domain.model.Note
import com.gramzin.noteapp.presentation.components.TextFieldComponent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EditNoteScreen(
    viewModel: EditNoteViewModel,
    onSaveNoteClick: () -> Unit
) {
    val backgroundColorState = animateColorAsState(Color(viewModel.noteColorState.value))

    if (viewModel.isFinishedState.value){
        onSaveNoteClick()
    }

    val toast = Toast.makeText(
        LocalContext.current,
        stringResource(R.string.fill_all_fields_toast),
        Toast.LENGTH_LONG
    )

    LaunchedEffect(key1 = Unit){
        viewModel.fieldsNotFilled.collectLatest {
            toast.show()
        }
    }
    
    Scaffold(
        modifier = Modifier,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier,
                onClick = {
                    viewModel.onEvent(EditNoteScreenEvent.SaveNote)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Save note"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColorState.value)
                .padding(paddingValues)
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Note.noteColors.forEach {
                    val modifier = if (it.toArgb() == viewModel.noteColorState.value){
                        Modifier.border(
                            width = 3.dp,
                            color = Color.White,
                            shape = CircleShape
                        )
                    } else {
                        Modifier
                    }
                    Box(modifier = modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(it)
                        .clickable {
                            viewModel.onEvent(EditNoteScreenEvent.ColorChanged(it.toArgb()))
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            val titleState = viewModel.titleState.value
            val focusManager = LocalFocusManager.current
            TextFieldComponent(
                text = titleState.text,
                isHintVisible = titleState.isHintVisible,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        viewModel.onEvent(EditNoteScreenEvent.ChangeTitleFocus(it))
                    },
                hint = stringResource(R.string.enter_title_hint),
                hintColor = Color.White,
                singleLine = true,
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    color = Color.White
                ),
                onValueChange = {
                    viewModel.onEvent(EditNoteScreenEvent.TitleEntered(it))
                },
                setFocusOnNextFieldAction = true,
                onNextAction = {
                    focusManager.moveFocus(FocusDirection.Down)
                },
                cursorColor = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))
            val contentState = viewModel.contentState.value
            TextFieldComponent(
                text = contentState.text,
                isHintVisible = contentState.isHintVisible,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        viewModel.onEvent(EditNoteScreenEvent.ChangeContentFocus(it))
                    },
                hint = stringResource(R.string.enter_some_content_hint),
                hintColor = Color.White,
                singleLine = false,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White
                ),
                onValueChange = {
                    viewModel.onEvent(EditNoteScreenEvent.ContentEntered(it))
                },
                cursorColor = Color.White
            )
        }
    }
}