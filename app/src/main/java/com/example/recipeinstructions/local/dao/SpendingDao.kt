package com.example.recipeinstructions.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.recipeinstructions.model.Favourite
import com.example.recipeinstructions.model.Instruction

@Dao
interface SpendingDao {
    @Query("SELECT * FROM Favourite")
    fun getAll(): LiveData<List<Favourite>>

    @Insert
    fun insertAll(vararg users: Favourite)

    @Delete
    fun delete(user: Favourite)
    @Query("SELECT * FROM Favourite")
    fun getAllData(): List<Favourite>

}