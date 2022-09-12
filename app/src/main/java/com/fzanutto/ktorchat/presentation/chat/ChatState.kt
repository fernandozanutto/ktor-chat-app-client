package com.fzanutto.ktorchat.presentation.chat

import com.fzanutto.ktorchat.domain.model.Message

data class ChatState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false
)
