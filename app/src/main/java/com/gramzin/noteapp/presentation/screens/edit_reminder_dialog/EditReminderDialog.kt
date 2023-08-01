package com.gramzin.noteapp.presentation.screens.edit_reminder_dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gramzin.noteapp.R
import com.gramzin.noteapp.presentation.components.CustomDatePickerDialog
import com.gramzin.noteapp.presentation.components.TextFieldComponent
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun EditReminderDialog(
    editDialogStateHandler: EditDialogStateHandler
){
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                editDialogStateHandler.onEvent(EditReminderEvent.OnCloseDialog)
            }
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {}
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .padding(top = 25.dp)
            ) {
                val titleState = editDialogStateHandler.titleState.collectAsState().value
                TextFieldComponent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged {
                            editDialogStateHandler.onEvent(EditReminderEvent.ChangeTitleFocus(it))
                        },
                    hint = stringResource(R.string.enter_the_text),
                    singleLine = false,
                    textStyle = MaterialTheme.typography.titleMedium,
                    hintColor = MaterialTheme.colorScheme.onSecondary,
                    onValueChange = {
                        editDialogStateHandler.onEvent(EditReminderEvent.TitleEntered(it))
                    },
                    text = titleState.text,
                    isHintVisible = titleState.isHintVisible,
                )

                Spacer(modifier = Modifier.height(30.dp))

                val selectedTime = editDialogStateHandler.selectedAlarmTime.collectAsState().value
                val buttonText = if (selectedTime!=null){
                    SimpleDateFormat("dd.MM HH:mm", Locale.getDefault())
                        .format(selectedTime)
                } else {
                    stringResource(R.string.set_alarm_time)
                }

                val isTimePickerVisible = editDialogStateHandler.isTimePickerDialogVisible
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        shape = RoundedCornerShape(10.dp),
                        onClick = {
                            editDialogStateHandler.onEvent(EditReminderEvent.TimePickerVisibleChange)
                        },
                    ) {
                        Text(
                            text = buttonText,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    val isBtnEnabled = editDialogStateHandler.isButtonEnabled.collectAsState().value
                    Text(
                        text = stringResource(R.string.save),
                        modifier = Modifier.clickable(enabled = isBtnEnabled) {
                            editDialogStateHandler.onEvent(EditReminderEvent.SaveReminder)
                        },
                        color = if (isBtnEnabled) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSecondary
                        }
                    )
                }

                if (isTimePickerVisible.value) {
                    CustomDatePickerDialog(
                        label = stringResource(R.string.alarm_time),
                        onDismissRequest = {
                            editDialogStateHandler
                                .onEvent(EditReminderEvent.TimePickerVisibleChange)
                        },
                        onDoneButtonClick = {
                            editDialogStateHandler
                                .onEvent(EditReminderEvent.DateTimeSelected(it.timeInMillis))
                        }
                    )
                }
            }
        }
    }
}