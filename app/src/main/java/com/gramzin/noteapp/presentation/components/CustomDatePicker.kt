package com.gramzin.noteapp.presentation.components

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.gramzin.noteapp.R
import java.util.Calendar
import java.util.Locale


@Composable
fun CustomDatePickerDialog(
    label: String,
    onDismissRequest: () -> Unit,
    onDoneButtonClick: (Calendar) ->Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        DatePickerUI(label, onDoneButtonClick)
    }
}

@Composable
fun DatePickerUI(
    label: String,
    onDismissRequest: (Calendar) -> Unit
) {
    Card(
        modifier = Modifier.padding(10.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 5.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            val chosenDate = remember {
                mutableStateOf(Calendar.getInstance())
            }

            val sdf = SimpleDateFormat("EEE, d MMM HH:mm", Locale.getDefault())
            Text(
                text = sdf.format(chosenDate.value.timeInMillis),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))


            DateSelectionSection(
                onDateChosen = { chosenDate.value = it },
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                onClick = {
                    onDismissRequest(chosenDate.value)
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = stringResource(R.string.done_button),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun DateSelectionSection(
    onDateChosen: (Calendar) -> Unit,
) {
    val sdf = SimpleDateFormat("EEE, d MMM", Locale.getDefault())
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(106.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val currentDate = remember {
            mutableStateOf(
                Calendar.getInstance().apply {
                    clear(Calendar.SECOND)
            })
        }
        val chosenDate = remember {
            mutableStateOf(currentDate.value)
        }

        LaunchedEffect(key1 = chosenDate.value){
            onDateChosen(chosenDate.value)
            val sdf2 = SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss ", Locale.getDefault())
            Log.d("TEST", sdf2.format(chosenDate.value.timeInMillis))
        }
        InfiniteItemsPicker(
            modifier = Modifier.weight(1f),
            onItemSelected =  {
                val calendar = currentDate.value.clone() as Calendar
                calendar.add(Calendar.DAY_OF_YEAR, it)
                val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
                val newDate = chosenDate.value.clone() as Calendar
                newDate.set(Calendar.DAY_OF_YEAR, dayOfYear)
                chosenDate.value = newDate
            },
            getNextItem = {
                val calendar = currentDate.value.clone() as Calendar
                calendar.add(Calendar.DAY_OF_YEAR, it)
                sdf.format(calendar.timeInMillis)
            }
        )

        val initialIndex = Int.MAX_VALUE / 2
        InfiniteItemsPicker(
            modifier = Modifier.weight(0.3f),
            onItemSelected =  {
                val offset = it - initialIndex
                val currentHour = currentDate.value.get(Calendar.HOUR_OF_DAY)
                val chosenHour = (24 + ((currentHour + offset) % 24)) % 24
                val newDate = chosenDate.value.clone() as Calendar
                newDate.set(Calendar.HOUR_OF_DAY, chosenHour)
                chosenDate.value = newDate
            },
            getNextItem = {
                val offset = it - initialIndex
                val currentHour = currentDate.value.get(Calendar.HOUR_OF_DAY)
                val chosenHour =  (24 + ((currentHour + offset) % 24)) % 24
                String.format("%02d", chosenHour)
            },
            initialIndex = initialIndex
        )
        InfiniteItemsPicker(
            modifier = Modifier.weight(0.3f),
            onItemSelected =  {
                val offset = it - initialIndex
                val currentMinute = currentDate.value.get(Calendar.MINUTE)
                val chosenMinutes = (60 + ((currentMinute + offset) % 60)) % 60
                val newDate = chosenDate.value.clone() as Calendar
                newDate.set(Calendar.MINUTE, chosenMinutes)
                chosenDate.value = newDate
            },
            getNextItem = {
                val offset = it - initialIndex
                val currentMinutes = currentDate.value.get(Calendar.MINUTE)
                val chosenMinutes =  (60 + ((currentMinutes + offset) % 60)) % 60
                String.format("%02d", chosenMinutes)
            },
            initialIndex = initialIndex
        )
    }
}

@Composable
fun InfiniteItemsPicker(
    modifier: Modifier = Modifier,
    getNextItem: (Int) -> String,
    onItemSelected: (Int) -> Unit,
    initialIndex: Int = 0,
    visibleItemsCount: Int = 3,
    boxHeight: Dp = 106.dp,
    spaceBy: Dp = 6.dp
) {

    val listState = rememberLazyListState(initialIndex)
    val currentValue = remember { mutableStateOf(1) }

    Box(modifier = modifier
        .height(boxHeight),
        contentAlignment = Alignment.Center
    ) {
        val textHeight = ((boxHeight - spaceBy * visibleItemsCount) / visibleItemsCount.toFloat()) * 0.745f


        LaunchedEffect(key1 = listState.isScrollInProgress) {
            onItemSelected(currentValue.value)
            listState.animateScrollToItem(index = listState.firstVisibleItemIndex)
        }

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spaceBy) ,
            state = listState,
            content = {

                item {
                    Spacer(Modifier.height(textHeight))
                }

                items(count = Int.MAX_VALUE - 500, itemContent = {
                    val isItemMain = it == remember {
                        derivedStateOf { listState.firstVisibleItemIndex }
                    }.value

                    if (isItemMain) currentValue.value = it

                    Text(
                        text = getNextItem(it),
                        modifier = Modifier
                            .alpha(if (isItemMain) 1f else 0.3f),
                        style = MaterialTheme.typography.bodySmall
                            .copy(fontSize = LocalDensity.current.run { textHeight.toSp() },),
                        textAlign = TextAlign.Center,
                    )
                })
            }
        )
    }
}



