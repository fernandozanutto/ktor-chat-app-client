package com.fzanutto.ktorchat.data.remote.user

import com.fzanutto.ktorchat.domain.model.User

interface UserService {
    suspend fun login(username: String): User?
    suspend fun signup(username: String): Boolean
}