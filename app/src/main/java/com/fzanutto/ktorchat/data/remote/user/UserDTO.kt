package com.fzanutto.ktorchat.data.remote.user

import com.fzanutto.ktorchat.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val username: String
) {
    fun toUser(): User {
        return User(username)
    }
}
