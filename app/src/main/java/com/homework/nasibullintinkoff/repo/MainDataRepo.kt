package com.homework.nasibullintinkoff.repo

import com.homework.nasibullintinkoff.App
import com.homework.nasibullintinkoff.data.PostDto
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

    fun getRemoteData(): Flow<Resource<PostDto>>{
        return flow {
            val result = safeApiCall { App.instance.apiService.getRandomPost() }
            val resultDto = Converters.fromPostResponsePostData(result)
            emit(resultDto)
        }.flowOn(Dispatchers.IO)
    }
}