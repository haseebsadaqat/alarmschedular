package com.example.task8kotlin.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class TimeStringDataModel(
    @PrimaryKey(autoGenerate = true)
    val id:Long, val time: String, val delAction: Boolean)
