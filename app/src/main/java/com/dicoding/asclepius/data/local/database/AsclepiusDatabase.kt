package com.dicoding.asclepius.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HistoryEntity::class], version = 3)
abstract class AsclepiusDatabase: RoomDatabase() {
    abstract fun historyAnalyzeDao(): HistoryAnalyzeDao

    companion object {
        @Volatile
        private var INSTANCE: AsclepiusDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): AsclepiusDatabase {
            if (INSTANCE == null) {
                synchronized(AsclepiusDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AsclepiusDatabase::class.java, "asclepius_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }

            return INSTANCE as AsclepiusDatabase
        }
    }
}