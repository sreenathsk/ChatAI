package com.chat.ai.model

import java.util.Date

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val createdDate: Date = Date()
)
