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
    fun getRemoteData(category: String, page: Long): Flow<Resource<List<PostDto>>>{
        return flow {
            val result = safeApiCall { App.instance.apiService.getRandomPost(category, page) }
            val resultDto = Converters.fromPostResponseToPostDto(result)
            emit(resultDto)
        }.flowOn(Dispatchers.IO)
    }

    /**
     * get local post data with safe request
     */
    fun getLocalData(id: Long, category: Long): Flow<Resource<List<PostDto>>>{
        return flow {
            val result = getSafeLocalData {
                AppDatabase.instance.postDao().getPostById(id, category)
            }
            val resultDto = Converters.fromPostDataToPostDto(result)
            emit(resultDto)
        }
    }

    /**
     * safe insert post data to database
     */
    suspend fun insertToDatabase(postDtoList: List<PostDto>, id: Long, category: Long){
        val postDataList = ArrayList<PostData>()
        postDtoList.forEachIndexed {index, element -> postDataList.add(
            PostData(
                id = null,
                postId = id+index,
                urlGif = element.urlGif.toString(),
                description = element.description,
                author = element.author,
                backId = element.id,
                categoryId = category
            )
        ) }
        updateSafeDatabase { AppDatabase.instance.postDao().insert(postDataList) }
    }

    /**
     * safe delete all data from posts table
     */
    suspend fun deleteAll(category: Long){
        updateSafeDatabase { AppDatabase.instance.postDao().deleteAll(category) }
    }
}