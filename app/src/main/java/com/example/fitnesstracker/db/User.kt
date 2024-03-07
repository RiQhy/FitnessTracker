package com.example.fitnesstracker.db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val height: Int,
    val weight: Int,
    val age: Int,
    val gender: String
)
