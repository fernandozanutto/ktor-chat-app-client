package com.fzanutto.ktorchat.presentation.username

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fzanutto.ktorchat.data.remote.user.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserNameViewModel @Inject constructor(
    private val userService: UserService
): ViewModel() {
    private val _usernameText = mutableStateOf("")
    val usernameText: State<String> = _usernameText

    private val _onLoginChat = MutableSharedFlow<String>()
    val onLoginChat = _onLoginChat.asSharedFlow()

    private val _onSignupUser = MutableSharedFlow<String>()
    val onSignupUser = _onSignupUser.asSharedFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    fun onUsernameChange(username: String) {
        _usernameText.value = username
    }

    fun signupUser() {
        _usernameText.value.let {
            viewModelScope.launch {
                val result = userService.signup(it)
                if (result) {
                    _onSignupUser.emit(it)
                } else {
                    _toastEvent.emit("Não foi possível fazer cadastro")
                }
            }
        }
    }

    fun loginUser() {
        _usernameText.value.let {
            viewModelScope.launch {
                val result = userService.login(it)
                if (result != null) {
                    _onLoginChat.emit(it)
                } else {
                    _toastEvent.emit("Não foi possível logar usuário")
                }
            }
        }
    }
}
