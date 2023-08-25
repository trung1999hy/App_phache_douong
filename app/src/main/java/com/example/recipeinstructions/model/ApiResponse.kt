package com.example.recipeinstructions.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.recipeinstructions.utils.Constants

data class ApiResponse<T>(
    val success: Boolean,
    val data: T,
    val message: String
)

data class Beverage(
    val _id: String,
    val type: String,
    val image: String
)

data class Instruction(
    val _id: String,
    val type_drink: String,
    val title: String,
    val image: String,
    val ingredient: List<String>,
    val intructions: List<String>
)

@Entity("Favourite")
data class Favourite(
    @PrimaryKey
    @ColumnInfo val _id: String,
    @ColumnInfo val type_drink: String,
    @ColumnInfo val title: String,
    @ColumnInfo val image: String,
)