package com.homework.nasibullintinkoff.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.homework.nasibullintinkoff.data.PostData

@Dao
interface PostDao {
    /**
     * get post by id from posts table
     * @param id is id of post
     * @return post data
     */
    @Query("SELECT * FROM posts WHERE post_id = :id AND category_id = :category")
    suspend fun getPostById(id: Long, category: Long): List<PostData>

    /**
     * insert new record for posts table
     * @param postDataList is list with new post data
     */
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(postDataList: List<PostData>)

    /**
     * delete records in the posts table by category
     */
    @Query("DELETE FROM posts WHERE category_id = :category")
    suspend fun deleteAll(category: Long)

}