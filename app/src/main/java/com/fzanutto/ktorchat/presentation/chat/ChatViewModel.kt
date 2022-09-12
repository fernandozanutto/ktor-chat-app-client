package com.fzanutto.ktorchat.presentation.chat

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fzanutto.ktorchat.data.remote.ChatSocketService
import com.fzanutto.ktorchat.data.remote.MessageService
import com.fzanutto.ktorchat.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageSerice: MessageService,
    private val chatSocketService: ChatSocketService,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _messageText = mutableStateOf("")
    val messageText: State<String> = _messageText

    private val _state = mutableStateOf(ChatState())
    val state: State<ChatState> = _state

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    fun connectToChat() {
        getAllMessages()
        savedStateHandle.get<String>("username")?.let {
            viewModelScope.launch {
                val result = chatSocketService.initSession(it)
                when (result) {
                    is Resource.Success -> {
                         chatSocketService.observeMessages()
                             .onEach { message ->
                                 val newList = state.value.messages.toMutableList().apply {
                                     add(0, message)
                                 }

                                 _state.value = state.value.copy(messages = newList)
                             }.launchIn(viewModelScope)
                    }
                    is Resource.Error -> {
                        _toastEvent.emit(result.message ?: "Unknown Erros")
                    }
                }
            }
        }
    }

    fun onMessageChange(message: String) {
        _messageText.value = message
    }

    fun disconnect() {
        viewModelScope.launch {
            chatSocketService.closeSession()
        }
    }

    fun sendMessage() {
        viewModelScope.launch {
            if (messageText.value.isNotBlank()) {
                chatSocketService.sendMessage(messageText.value)
            }
        }
    }

    fun getAllMessages() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val result = messageSerice.getAllMessages()
            _state.value = _state.value.copy(messages = result, isLoading = false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}