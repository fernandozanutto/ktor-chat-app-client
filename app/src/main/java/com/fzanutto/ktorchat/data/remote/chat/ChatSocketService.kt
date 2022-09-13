package com.fzanutto.ktorchat.data.remote.chat

import com.fzanutto.ktorchat.data.remote.RemoteConstants
import com.fzanutto.ktorchat.domain.model.Message
import com.fzanutto.ktorchat.util.Resource
import kotlinx.coroutines.flow.Flow

interface ChatSocketService {
    suspend fun initSession(
        username: String
    ): Resource<Unit>

    suspend fun sendMessage(
        message: String
    )

    fun observeMessages(): Flow<Message>

    suspend fun closeSession()

    sealed class Endpoints(val url: String) {
        object ChatSocket: Endpoints("${RemoteConstants.CHAT_BASE_URL}/chat-socket")
    }
}