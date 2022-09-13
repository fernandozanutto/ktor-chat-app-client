package com.fzanutto.ktorchat.presentation.username

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun UsernameScreen(
    viewModel: UserNameViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
    LaunchedEffect(key1 = true) {
        viewModel.onLoginChat.collectLatest { username ->
            onNavigate("chat_screen/$username")
        }
    }

    ScreenContent(
        viewModel::onUsernameChange,
        viewModel.usernameText.value,
        viewModel::loginUser,
        viewModel::signupUser
    )
}

@Preview
@Composable
private fun ScreenContent(
    onUsernameChange: (String) -> Unit = {},
    usernameValue: String = "Username",
    onLoginButtonClick: () -> Unit = {},
    onSignupButtonClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = usernameValue,
                onValueChange = onUsernameChange,
                placeholder = {
                    Text(text = "Enter a username...")
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = onSignupButtonClick) {
                    Text(text = "Signup")
                }
                Button(onClick = onLoginButtonClick) {
                    Text(text = "Login")
                }
            }
        }
    }
}