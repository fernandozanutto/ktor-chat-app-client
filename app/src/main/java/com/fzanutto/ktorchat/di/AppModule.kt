package com.fzanutto.ktorchat.di

import com.fzanutto.ktorchat.data.remote.chat.ChatSocketService
import com.fzanutto.ktorchat.data.remote.chat.ChatSocketServiceImpl
import com.fzanutto.ktorchat.data.remote.message.MessageService
import com.fzanutto.ktorchat.data.remote.message.MessageServiceImpl
import com.fzanutto.ktorchat.data.remote.user.UserService
import com.fzanutto.ktorchat.data.remote.user.UserServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.websocket.WebSockets
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(Logging)
            install(WebSockets)
            install(JsonFeature) {
                val customJson = Json {
                    isLenient = false
                    ignoreUnknownKeys = true
                    allowSpecialFloatingPointValues = true
                    useArrayPolymorphism = false
                }
                serializer = KotlinxSerializer(customJson)
            }
        }
    }

    @Provides
    @Singleton
    fun provideMessageService(client: HttpClient): MessageService {
        return MessageServiceImpl(client)
    }

    @Provides
    @Singleton
    fun provideUserService(client: HttpClient): UserService {
        return UserServiceImpl(client)
    }

    @Provides
    @Singleton
    fun provideChatSocketService(client: HttpClient): ChatSocketService {
        return ChatSocketServiceImpl(client)
    }
}