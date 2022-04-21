package com.example.quintrixgroupproject.translation

data class TranslateResponse(
    val code: Int = 0,
    val lang: String,
    val text: Array<String>)