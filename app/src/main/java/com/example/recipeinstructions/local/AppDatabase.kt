package com.example.recipeinstructions.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.recipeinstructions.local.dao.SpendingDao
import com.example.recipeinstructions.model.Favourite
import com.example.recipeinstructions.model.Instruction

@Database(
    entities = [Favourite::class, ],
    version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getSpendingDao(): SpendingDao

    companion object {
        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(
                        context, AppDatabase::class.java, "database-name"
                    ).allowMainThreadQueries().build()

            }
            return instance!!
        }
    }
}