package com.gox.base.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LocationPointsEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun locationPointsDao(): LocationPointsDao

    companion object {
        var INSTANCE: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null) synchronized(AppDatabase::class) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "LocationPointsDB")
                        .allowMainThreadQueries().fallbackToDestructiveMigration().build()
            }
            return INSTANCE
        }

        fun destroyDataBase() {
            INSTANCE = null
        }
    }
}