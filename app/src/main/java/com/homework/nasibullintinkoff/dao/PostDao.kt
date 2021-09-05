package com.homework.nasibullintinkoff.dao

import androidx.room.*
import com.homework.nasibullintinkoff.data.PostData

@Dao
interface PostDao {
    /**
     * get post by id from posts table
     * @param id is id of post
     * @return post data
     */
    @Query("SELECT * FROM posts WHERE post_id = :id")
    suspend fun getPostById(id: Long): PostData

    /**
     * insert new record for posts table
     * @param postData is new record with new post data
     */
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(postData: PostData)

    /**
     * delete all records in the table
     */
    @Query("DELETE FROM posts")
    suspend fun deleteAll()
}