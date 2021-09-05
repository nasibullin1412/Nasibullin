package com.homework.nasibullintinkoff.data

import java.lang.StringBuilder

/**
 * data class of post abstraction,
 * which is used in business logic
 */
data class PostDto(
    val id: Long,
    val urlGif: StringBuilder,
    val description: String,
    val author:String
)