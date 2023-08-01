package com.gramzin.noteapp.presentation.screens.reminders_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gramzin.noteapp.R
import com.gramzin.noteapp.domain.utils.OrderType
import com.gramzin.noteapp.domain.utils.ReminderOrder
import com.gramzin.noteapp.presentation.components.RadioButtonComponent

@Composable
fun ReminderOrderSection(
    modifier: Modifier = Modifier,
    reminderOrder: ReminderOrder = ReminderOrder.ReminderDate(OrderType.Descending),
    onOrderChange: (ReminderOrder) -> Unit
) {
    Column(modifier = modifier) {
        Column() {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                RadioButtonComponent(
                    text = stringResource(R.string.reminder_time_order),
                    onSelect = { onOrderChange(ReminderOrder.ReminderDate(reminderOrder.orderType)) },
                    selected = reminderOrder is ReminderOrder.ReminderDate
                )
                RadioButtonComponent(
                    text = stringResource(R.string.creation_time_order),
                    onSelect = { onOrderChange(ReminderOrder.CreationDate(reminderOrder.orderType)) },
                    selected = reminderOrder is ReminderOrder.CreationDate
                )
            }
            RadioButtonComponent(
                text = stringResource(R.string.title_order_button),
                onSelect = { onOrderChange(ReminderOrder.Title(reminderOrder.orderType)) },
                selected = reminderOrder is ReminderOrder.Title
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            RadioButtonComponent(
                text = stringResource(R.string.descending_order_button),
                onSelect = { onOrderChange(reminderOrder.copy(OrderType.Descending)) },
                selected = reminderOrder.orderType is OrderType.Descending
            )
            RadioButtonComponent(
                text = stringResource(R.string.ascending_orderr_button),
                onSelect = { onOrderChange(reminderOrder.copy(OrderType.Ascending)) },
                selected = reminderOrder.orderType is OrderType.Ascending
            )
        }
    }
}