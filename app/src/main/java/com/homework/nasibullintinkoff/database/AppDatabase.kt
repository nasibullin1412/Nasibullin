package com.homework.nasibullintinkoff.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.homework.nasibullintinkoff.App
import com.homework.nasibullintinkoff.dao.PostDao
import com.homework.nasibullintinkoff.data.PostData

@Database(entities = [
    PostData::class
],
version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun postDao(): PostDao
    companion object {
        private const val DATABASE_NAME = "Posts.db"
        val instance: AppDatabase by lazy {
            Room.databaseBuilder(
                App.appContext,
                AppDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}