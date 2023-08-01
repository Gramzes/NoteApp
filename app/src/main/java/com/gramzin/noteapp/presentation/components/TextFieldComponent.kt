package com.gramzin.noteapp.presentation.components

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization

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
    setFocusOnNextFieldAction: Boolean = false,
    onNextAction: (KeyboardActionScope.() -> Unit)? = null,
    cursorColor: Color? = null,
) {
    val imeAction = if (setFocusOnNextFieldAction){
        ImeAction.Next
    } else {
        ImeAction.Default
    }
    BasicTextField(
        modifier = modifier,
        value = text,
        onValueChange = onValueChange,
        textStyle = textStyle,
        singleLine = singleLine,
        cursorBrush = SolidColor(cursorColor ?: Color.Black),
        decorationBox = { innerTextField ->
            if(isHintVisible) {
                Text(
                    text = hint,
                    style = textStyle.copy(
                        color = hintColor
                    ),
                    color = hintColor
                )
            }
            innerTextField()
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onNext = onNextAction
        ),
    )
}