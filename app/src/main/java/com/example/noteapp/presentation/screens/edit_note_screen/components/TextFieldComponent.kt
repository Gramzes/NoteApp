package com.example.noteapp.presentation.screens.edit_note_screen.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldComponent(
    modifier: Modifier,
    text: String = "",
    hint: String,
    isHintVisible: Boolean = true,
    singleLine: Boolean,
    textStyle: TextStyle,
    hintColor: Color,
    onValueChange: (String) -> Unit,
) {
    BasicTextField(
        modifier = modifier,
        value = text,
        onValueChange = onValueChange,
        textStyle = textStyle,
        singleLine = singleLine,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.inverseOnSurface),
        decorationBox = { innerTextField ->
            if(isHintVisible) {
                Text(
                    text = hint,
                    style = textStyle.copy(
                        color = hintColor
                    ),
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
            }
            innerTextField()
        }
    )
}