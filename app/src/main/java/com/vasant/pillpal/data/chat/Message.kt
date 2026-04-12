package com.PillPal.data.chat

import com.PillPal.APPCONSTANTS.BOT
import com.PillPal.APPCONSTANTS.MY_ID

data class Message(
    val text: String,
    val author: Author,
) {
    val isFromMe: Boolean
        get() = author== Author.ME

    companion object {
        val initConv = Message(
            text = "Hi there, how you doing?",
            author = Author.BOT)
    }
}
