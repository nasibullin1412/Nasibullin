package com.homework.nasibullintinkoff.data

import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.TEXT
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.lang.StringBuilder

@Entity(tableName = "posts")
data class PostData(
    @PrimaryKey
    @ColumnInfo(name = "post_id")
    val id: Long,
    @ColumnInfo(name = "url_gif", typeAffinity = TEXT)
    val gifUrl: String,
    @ColumnInfo(name = "description", typeAffinity = TEXT)
    val description: String,
    @ColumnInfo(name = "author", typeAffinity = TEXT)
    val author: String
)