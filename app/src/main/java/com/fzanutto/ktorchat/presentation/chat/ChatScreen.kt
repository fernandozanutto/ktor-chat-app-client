package com.fzanutto.ktorchat.presentation.chat

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.fzanutto.ktorchat.domain.model.Message
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChatScreen(
    username: String?,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.toastEvent.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.connectToChat()
            } else if (event == Lifecycle.Event.ON_STOP) {
                viewModel.disconnect()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val state = viewModel.state.value
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            reverseLayout = true
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            items(state.messages) { message ->
                val isOwnMessage = message.username == username
                Spacer(modifier = Modifier.height(24.dp))
                ChatMessage(isOwnMessage, message)
            }
        }
        
        Row(
           modifier = Modifier.fillMaxWidth() 
        ) {
            TextField(
                value = viewModel.messageText.value,
                onValueChange = viewModel::onMessageChange,
                placeholder = {
                    Text(text = "Enter a message")
                },
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = viewModel::sendMessage) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send"
                )
            }
        }
    }
}

@Preview(
    showSystemUi = true
)
@Composable
private fun PreviewChatMessage() {
    val message = Message("Hello, how you doing?", "19h25", "Fernando")
    Column {
        ChatMessage(isOwnMessage = true, message = message)
        Spacer(modifier = Modifier.height(16.dp))
        ChatMessage(isOwnMessage = false, message = message)
    }
}

@Composable
private fun ChatMessage(
    isOwnMessage: Boolean,
    message: Message
) {
    Box(
        contentAlignment = if (isOwnMessage) {
            Alignment.CenterEnd
        } else Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        val backgroundColor = if (isOwnMessage) {
            Color(red = 0x07, green = 0x67, blue = 0x4B)
        } else Color(red = 0x22, green = 0x2C, blue = 0x32)

        val cornerRadiusDp = 10.dp
        Column(
            modifier = Modifier
                .width(200.dp)
                .drawBehind {
                    val cornerRadius = cornerRadiusDp.toPx()
                    val triangleHeight = 16.dp.toPx()
                    val triangleWidth = 12.dp.toPx()

                    val trianglePath = Path().apply {
                        if (isOwnMessage) {
                            moveTo(size.width - cornerRadius, 0f)
                            lineTo(size.width + triangleWidth, 0f)
                            lineTo(size.width, triangleHeight)
                        } else {
                            moveTo(cornerRadius, 0f)
                            lineTo(-triangleWidth, 0f)
                            lineTo(0f, triangleHeight)
                        }
                        close()
                    }
                    drawPath(
                        path = trianglePath,
                        color = backgroundColor
                    )
                }
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(cornerRadiusDp)
                )
                .padding(8.dp)
        ) {
            if (!isOwnMessage) {
                Text(message.username, fontWeight = FontWeight.Bold, color = Color(0xFFCF5B0E))
            }

            Text(message.text, color = Color.White)
            Text(
                message.formattedTime,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.End)
            )
        }
    }
}