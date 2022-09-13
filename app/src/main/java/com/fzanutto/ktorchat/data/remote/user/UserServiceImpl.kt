package com.fzanutto.ktorchat.data.remote.user

import com.fzanutto.ktorchat.data.remote.message.MessageService
import com.fzanutto.ktorchat.domain.model.User
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class UserServiceImpl(
    private val client: HttpClient
) : UserService {
    override suspend fun login(username: String): User? {
        return try {
            val call = client.get<UserDTO?>(
                MessageService.Endpoints.Login.url
            ) {
                this.parameter("username", username)
            }

            call?.toUser()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun signup(username: String): Boolean {
        return try {
            client.get<Boolean>(
                MessageService.Endpoints.Signup.url
            ) {
                this.parameter("username", username)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}