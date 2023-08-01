package com.gramzin.noteapp.presentation.screens.notes_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gramzin.noteapp.R
import com.gramzin.noteapp.domain.utils.NoteOrder
import com.gramzin.noteapp.domain.utils.OrderType
import com.gramzin.noteapp.presentation.components.RadioButtonComponent

@Composable
fun NoteOrderSection(
    modifier: Modifier = Modifier,
    noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    onOrderChange: (NoteOrder) -> Unit
) {
    Column(modifier = modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            RadioButtonComponent(
                text = stringResource(R.string.date_order_button),
                onSelect = { onOrderChange(NoteOrder.Date(noteOrder.orderType)) },
                selected = noteOrder is NoteOrder.Date
            )
            RadioButtonComponent(
                text = stringResource(R.string.title_order_button),
                onSelect = { onOrderChange(NoteOrder.Title(noteOrder.orderType)) },
                selected = noteOrder is NoteOrder.Title
            )
            RadioButtonComponent(
                text = stringResource(R.string.color_order_button),
                onSelect = { onOrderChange(NoteOrder.Color(noteOrder.orderType)) },
                selected = noteOrder is NoteOrder.Color
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            RadioButtonComponent(
                text = stringResource(R.string.descending_order_button),
                onSelect = { onOrderChange(noteOrder.copy(OrderType.Descending)) },
                selected = noteOrder.orderType is OrderType.Descending
            )
            RadioButtonComponent(
                text = stringResource(R.string.ascending_orderr_button),
                onSelect = { onOrderChange(noteOrder.copy(OrderType.Ascending)) },
                selected = noteOrder.orderType is OrderType.Ascending
            )
        }
    }
}