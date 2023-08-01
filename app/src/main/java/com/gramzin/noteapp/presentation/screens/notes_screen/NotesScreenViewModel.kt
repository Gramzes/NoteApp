package com.gramzin.noteapp.presentation.screens.notes_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramzin.noteapp.domain.model.Note
import com.gramzin.noteapp.domain.use_case.DeleteNoteUseCase
import com.gramzin.noteapp.domain.use_case.GetNotesUseCase
import com.gramzin.noteapp.domain.use_case.AddNoteUseCase
import com.gramzin.noteapp.domain.utils.NoteOrder
import com.gramzin.noteapp.domain.utils.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesScreenViewModel @Inject constructor(
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val getNotesUseCase: GetNotesUseCase,
    private val addNoteUseCase: AddNoteUseCase
): ViewModel() {

    private var flowJob: Job = Job()
    private var lastDeletedNote: Note? = null
    private val _state = mutableStateOf(NotesScreenState(listOf()))
    val state: State<NotesScreenState> = _state

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesScreenEvent){
        when(event){
            is NotesScreenEvent.DeleteNote -> {
                viewModelScope.launch {
                    lastDeletedNote = event.note
                    deleteNoteUseCase(event.note)
                }
            }
            is NotesScreenEvent.Order -> {
                if (_state.value.noteOrder != event.noteOrder){
                    getNotes(event.noteOrder)
                }
            }
            NotesScreenEvent.RestoreNote -> {
                viewModelScope.launch {
                    lastDeletedNote?.let {
                        addNoteUseCase(it)
                        lastDeletedNote = null
                    }
                }
            }
            NotesScreenEvent.ToggleOrderSection -> {
                _state.value = _state.value.copy(
                    isOrderSelectionVisible = !_state.value.isOrderSelectionVisible
                )
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder){
        flowJob.cancel()
        flowJob = getNotesUseCase(noteOrder)
            .onEach {
                _state.value = _state.value.copy(
                    notes = it,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }
}