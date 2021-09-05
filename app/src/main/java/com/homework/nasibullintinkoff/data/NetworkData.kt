package com.homework.nasibullintinkoff.data

import kotlinx.serialization.Serializable

/**
 * data class of post abstraction,
 * which is used when receiving data from the network
 */
@Serializable
data class PostResponse(
    val id: Long,
    val description: String,
    val author: String,
    val gifURL: String
)