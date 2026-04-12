package com.PillPal.data.api.body

import com.PillPal.data.chat.GenerationParameters

data class ChatRequest(
    val inputs: String,
    val parameters: GenerationParameters = GenerationParameters()
)
