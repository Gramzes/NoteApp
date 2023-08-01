package com.gramzin.noteapp.presentation.screens.reminders_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gramzin.noteapp.R
import com.gramzin.noteapp.domain.model.Reminder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderItem(
    reminder: Reminder,
    cornerSize: Dp,
    modifier: Modifier = Modifier,
    onDeleteReminder: () -> Unit,
    onItemClick: () -> Unit,
    onCompletedChange: (Boolean) -> Unit,
){
    val dismissState = rememberDismissState(
        confirmValueChange = {
            if (it == DismissValue.DismissedToStart){
                onDeleteReminder()
                true
            } else{
                false
            }
        },
        positionalThreshold = {
            60.dp.toPx()
        }
    )
    SwipeToDismiss(
        modifier = modifier,
        state = dismissState,
        directions = setOf(DismissDirection.EndToStart),
        background = {
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(cornerSize + 1.dp),
                colors = CardDefaults
                    .cardColors(
                        containerColor = Color.Red
                    )
            ){
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp)
                    ,
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_reminder_icon),
                        tint = MaterialTheme.colorScheme.surface
                    )
                }
            }
        },
        dismissContent = {
            ReminderItemContent(
                reminder = reminder,
                cornerSize = cornerSize,
                onItemClick = onItemClick,
                onCompletedChange = onCompletedChange
            )
        }
    )
}

@Composable
private fun ReminderItemContent(
    reminder: Reminder,
    cornerSize: Dp,
    onItemClick: () -> Unit,
    onCompletedChange: (Boolean) -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() },
        shape = RoundedCornerShape(cornerSize),
        colors = CardDefaults
            .cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = if (!reminder.isCompleted) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSecondary
                }
            )
    ) {

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = reminder.isCompleted,
                onCheckedChange = {
                    onCompletedChange(it)
                }
            )
            Column {
                Text(
                    text = reminder.reminder,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(5.dp))
                
                val sdf = SimpleDateFormat("dd.MM HH:mm", Locale.getDefault())
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = sdf.format(Date(reminder.reminderTimestamp)),
                        style = MaterialTheme.typography.bodySmall,
                    )
                    if (reminder.isExpired){
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.expired),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                    Spacer(modifier = Modifier.width(7.dp))
                    Icon(
                        modifier = Modifier.size(10.dp),
                        imageVector = ImageVector
                            .vectorResource(
                                id = R.drawable.alarm_icon),
                        contentDescription = stringResource(R.string.alarm),
                        tint = if (!reminder.isCompleted){
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSecondary
                        }
                    )
                }
            }
        }
    }
}