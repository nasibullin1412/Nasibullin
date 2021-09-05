package com.homework.nasibullintinkoff.repo

import com.homework.nasibullintinkoff.App
import com.homework.nasibullintinkoff.data.PostData
import com.homework.nasibullintinkoff.data.PostDto
import com.homework.nasibullintinkoff.database.AppDatabase
import com.homework.nasibullintinkoff.utils.BaseDataSource
import com.homework.nasibullintinkoff.utils.Converters
import com.homework.nasibullintinkoff.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainDataRepo @Inject constructor(): BaseDataSource() {

    /**
     * get remote post data with safe request
     */
    fun getRemoteData(): Flow<Resource<PostDto>>{
        return flow {
            val result = safeApiCall { App.instance.apiService.getRandomPost() }
            val resultDto = Converters.fromPostResponseToPostDto(result)
            emit(resultDto)
        }.flowOn(Dispatchers.IO)
    }

    /**
     * get local post data with safe request
     */
    fun getLocalData(id: Long): Flow<Resource<PostDto>>{
        return flow {
            val result = getSafeLocalData {
                AppDatabase.instance.postDao().getPostById(id)
            }
            val resultDto = Converters.fromPostDataToPostDto(result)
            emit(resultDto)
        }
    }

    /**
     * safe insert post data to database
     */
    suspend fun insertToDatabase(postDto: PostDto, id: Long){
        val postData = PostData(
            id = id,
            urlGif = postDto.urlGif.toString(),
            description = postDto.description,
            author = postDto.author,
            backId = postDto.id
        )
        updateSafeDatabase { AppDatabase.instance.postDao().insert(postData) }
    }

    /**
     * safe delete all data from posts table
     */
    suspend fun deleteAll(){
        updateSafeDatabase { AppDatabase.instance.postDao().deleteAll() }
    }
}