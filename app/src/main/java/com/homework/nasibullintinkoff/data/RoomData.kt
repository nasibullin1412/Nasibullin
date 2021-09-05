package com.homework.nasibullintinkoff.data

import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.INTEGER
import androidx.room.ColumnInfo.TEXT
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * data class of post abstraction,
 * which is used with room
 */
@Entity(tableName = "posts")
data class PostData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long?,
    @ColumnInfo(name = "post_id")
    val postId: Long,
    @ColumnInfo(name = "url_gif", typeAffinity = TEXT)
    val urlGif: String,
    @ColumnInfo(name = "description", typeAffinity = TEXT)
    val description: String,
    @ColumnInfo(name = "author", typeAffinity = TEXT)
    val author: String,
    @ColumnInfo(name = "back_id", typeAffinity = INTEGER)
    val backId: Long,
    @ColumnInfo(name = "category_id", typeAffinity = INTEGER)
    val categoryId: Long
)