package com.fzanutto.ktorchat.data.remote

import com.fzanutto.ktorchat.domain.model.Message

interface MessageService {
    suspend fun getAllMessages(): List<Message>

    sealed class Endpoints(val url: String) {
        object GetAllMessages: Endpoints("${RemoteConstants.MESSAGE_BASE_URL}/messages")
    }
}