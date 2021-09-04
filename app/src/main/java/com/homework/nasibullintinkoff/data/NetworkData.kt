package com.homework.nasibullintinkoff.data

import kotlinx.serialization.Serializable

@Serializable
data class PostResponse(
    val id: Long,
    val description: String,
    val author: String,
    val gifURL: String
)