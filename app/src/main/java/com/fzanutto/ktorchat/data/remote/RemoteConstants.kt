package com.fzanutto.ktorchat.data.remote

import com.fzanutto.ktorchat.util.RuntimeUtis

object RemoteConstants {
    val CHAT_BASE_URL = if (RuntimeUtis.isEmulator()) {
        "ws://10.0.2.2:8080"
    } else {
        "ws://192.168.1.9:8080"
    }
    val MESSAGE_BASE_URL = if (RuntimeUtis.isEmulator()) {
        "http://10.0.2.2:8080"
    } else {
        "http://192.168.1.9:8080"
    }
}