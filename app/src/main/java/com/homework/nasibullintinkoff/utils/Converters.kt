package com.homework.nasibullintinkoff.utils

import com.homework.nasibullintinkoff.data.PostData
import com.homework.nasibullintinkoff.data.PostDto
import com.homework.nasibullintinkoff.data.PostResponse
import java.lang.StringBuilder

object Converters {
    private const val ERROR_CONVERT = "Error convert"

    fun fromPostResponseToPostDto(postResponseList: Resource<PostResponse>)
    : Resource<List<PostDto>> =
        postResponseList.data?.let {
            Resource.success(
                it.result.map { postResponseItem ->
                    PostDto(
                        id = postResponseItem.id,
                        urlGif = StringBuilder(postResponseItem.gifURL).insert(4, 's'),
                        description = postResponseItem.description,
                        author = postResponseItem.author
                    )
                }
            )
        }?:
        if (Resource.Status.FAILURE == postResponseList.status) Resource.failed(
            postResponseList.message?: ERROR_CONVERT
        )
        else Resource.error(
            postResponseList.message?: ERROR_CONVERT
        )

    fun fromPostDataToPostDto(postDataList: Resource<List<PostData>>)
    : Resource<List<PostDto>> =
        postDataList.data?.let {
            Resource.success(
                it.map { postItem ->
                    PostDto(
                        id = postItem.postId,
                        urlGif = StringBuilder(postItem.urlGif),
                        description = postItem.description,
                        author = postItem.author
                    )
                }
            )
        }?:
        if (Resource.Status.FAILURE == postDataList.status) Resource.failed(
            postDataList.message?: ERROR_CONVERT
        )
        else Resource.error(
            postDataList.message?: ERROR_CONVERT
        )
}