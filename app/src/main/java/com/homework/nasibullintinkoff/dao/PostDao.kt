package com.homework.nasibullintinkoff.dao

import androidx.room.*
import com.homework.nasibullintinkoff.data.PostData

@Dao
interface PostDao {
    @Query("SELECT * FROM posts WHERE post_id = :id")
    suspend fun getPostById(id: Long): PostData

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(postData: PostData)

    @Query("DELETE FROM posts")
    suspend fun deleteAll()
}