package com.fzanutto.ktorchat.data.remote.message

import com.fzanutto.ktorchat.data.remote.RemoteConstants
import com.fzanutto.ktorchat.domain.model.Message

interface MessageService {
    suspend fun getAllMessages(): List<Message>

    sealed class Endpoints(val url: String) {
        object GetAllMessages: Endpoints("${RemoteConstants.MESSAGE_BASE_URL}/messages")
        object GetAllRooms: Endpoints("${RemoteConstants.MESSAGE_BASE_URL}/rooms")
        object Signup: Endpoints("${RemoteConstants.MESSAGE_BASE_URL}/signup")
        object Login: Endpoints("${RemoteConstants.MESSAGE_BASE_URL}/login")
    }
}