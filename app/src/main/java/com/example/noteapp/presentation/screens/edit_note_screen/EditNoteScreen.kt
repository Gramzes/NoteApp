package com.example.noteapp.presentation.screens.edit_note_screen

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.noteapp.R
import com.example.noteapp.domain.model.Note
import com.example.noteapp.presentation.screens.edit_note_screen.components.TextFieldComponent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    viewModel: EditNoteViewModel,
    onSaveNoteClick: () -> Unit
) {
    val backgroundColorState = animateColorAsState(Color(viewModel.noteColorState.value))

    if (viewModel.isFinishedState.value){
        onSaveNoteClick()
    }

    val toast = Toast.makeText(LocalContext.current, "Fill in all the fields", Toast.LENGTH_LONG)
    LaunchedEffect(key1 = true){
        viewModel.fieldsNotFilled.collectLatest {
            toast.show()
        }
    }
    
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { 
                viewModel.onEvent(EditNoteScreenEvent.SaveNote)
            }) {
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
                    var modifier = if (it.toArgb() == viewModel.noteColorState.value){
                        Modifier.border(
                            width = 3.dp,
                            color = MaterialTheme.colorScheme.inverseOnSurface,
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
            TextFieldComponent(
                text = titleState.text,
                isHintVisible = titleState.isHintVisible,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        viewModel.onEvent(EditNoteScreenEvent.ChangeTitleFocus(it))
                    },
                hint = stringResource(R.string.enter_title_hint),
                hintColor = MaterialTheme.colorScheme.onSecondary,
                singleLine = true,
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
            ){
                viewModel.onEvent(EditNoteScreenEvent.TitleEntered(it))
            }

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
                hintColor = MaterialTheme.colorScheme.onSecondary,
                singleLine = false,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
            ){
                viewModel.onEvent(EditNoteScreenEvent.ContentEntered(it))
            }
        }
    }
}