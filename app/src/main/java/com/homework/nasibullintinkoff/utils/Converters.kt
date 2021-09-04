package com.homework.nasibullintinkoff.utils

import com.homework.nasibullintinkoff.data.PostDto
import com.homework.nasibullintinkoff.data.PostResponse
import java.lang.StringBuilder

object Converters {
    private const val ERROR_CONVERT = "Error convert"

    fun fromPostResponsePostData(postResponse: Resource<PostResponse>)
    : Resource<PostDto> =
        postResponse.data?.let {
            Resource.success(
                PostDto(
                    id = postResponse.data.id,
                    urlGif = StringBuilder(postResponse.data.gifURL).insert(4, 's'),
                    description = postResponse.data.description,
                    author = postResponse.data.author
                )
            )
        }?:
        if (Resource.Status.FAILURE == postResponse.status) Resource.failed(
            postResponse.message?: ERROR_CONVERT
        )
        else Resource.error(
            postResponse.message?: ERROR_CONVERT
        )
}