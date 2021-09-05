package com.homework.nasibullintinkoff.data

import kotlinx.serialization.Serializable

@Serializable
data class PostResponse(
    val result: List<PostResponseItem>
)

/**
 * data class of post abstraction,
 * which is used when receiving data from the network
 */
@Serializable
data class PostResponseItem(
    val id: Long,
    val description: String,
    val author: String,
    val gifURL: String
)