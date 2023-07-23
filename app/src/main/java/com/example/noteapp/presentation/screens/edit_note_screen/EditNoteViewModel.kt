package com.example.noteapp.presentation.screens.edit_note_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.use_case.AddNoteUseCase
import com.example.noteapp.domain.use_case.GetNoteByIdUseCase
import com.example.noteapp.domain.utils.FieldsNotFieldException
import com.example.noteapp.presentation.utils.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val addNoteUseCase: AddNoteUseCase,
    getNoteByIdUseCase: GetNoteByIdUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private var currentNoteId: Int? = null

    private val _titleState = mutableStateOf(TextFieldState("Enter title..."))
    val titleState: State<TextFieldState> = _titleState

    private val _contentState = mutableStateOf(TextFieldState("Enter content..."))
    val contentState: State<TextFieldState> = _contentState

    private val _noteColorState = mutableStateOf(Note.noteColors.random().toArgb())
    val noteColorState: State<Int> = _noteColorState

    private val _isFinishedState = mutableStateOf(false)
    val isFinishedState: State<Boolean> = _isFinishedState

    private val _fieldsNotFilled = MutableSharedFlow<Boolean>()
    val fieldsNotFilled = _fieldsNotFilled.asSharedFlow()

    init {
        val noteId = savedStateHandle.get<Int>(Screen.EditNote.NOTE_ID_KEY)!!
        if(noteId != -1) {
            val noteFlow = getNoteByIdUseCase(noteId)
            noteFlow
                .filterNotNull()
                .onEach { note ->
                    currentNoteId = note.id
                    _titleState.value = _titleState.value
                        .copy(
                            text = note.title,
                            isHintVisible = note.title.isEmpty()
                        )
                    _contentState.value = _contentState.value.copy(
                        text = note.content,
                        isHintVisible = note.content.isEmpty()
                    )
                    _noteColorState.value = note.color
                }.launchIn(viewModelScope)
        }
    }

    fun onEvent(event: EditNoteScreenEvent) {
        when(event){
            is EditNoteScreenEvent.ChangeContentFocus -> {
                _contentState.value = _contentState.value.copy(
                    isHintVisible = checkIsHintVisible(_contentState.value.text, event.focusState)
                )
            }
            is EditNoteScreenEvent.ChangeTitleFocus -> {
                _titleState.value = _titleState.value.copy(
                    isHintVisible = checkIsHintVisible(_titleState.value.text, event.focusState)
                )
            }
            is EditNoteScreenEvent.ColorChanged -> {
                _noteColorState.value = event.color
            }
            is EditNoteScreenEvent.ContentEntered -> {
                _contentState.value = _contentState.value.copy(
                    text = event.text
                )
            }
            is EditNoteScreenEvent.TitleEntered -> {
                _titleState.value = _titleState.value.copy(
                    text = event.text
                )
            }
            EditNoteScreenEvent.SaveNote -> {
                val note = Note(
                    _titleState.value.text,
                    _contentState.value.text,
                    _noteColorState.value,
                    System.currentTimeMillis(),
                    currentNoteId
                )
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        addNoteUseCase(note)
                        _isFinishedState.value = true
                    } catch (ex: FieldsNotFieldException){
                        _fieldsNotFilled.emit(true)
                    }
                }
            }
        }
    }

    private fun checkIsHintVisible(text: String, focus: FocusState): Boolean{
        return !(focus.isFocused || text.isNotEmpty())
    }
}