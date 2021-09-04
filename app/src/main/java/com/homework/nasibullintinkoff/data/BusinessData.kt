package com.homework.nasibullintinkoff.data

import java.lang.StringBuilder

data class PostDto(
    val id: Long,
    val urlGif: StringBuilder,
    val description: String,
    val author:String
)