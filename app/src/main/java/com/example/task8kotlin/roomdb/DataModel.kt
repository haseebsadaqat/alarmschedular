package com.example.task8kotlin.roomdb
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
@Entity(tableName = "users")
data class DataModel(@PrimaryKey(autoGenerate = true)
                     val id:Long=0,
                     val time:LocalDateTime,
                     val delAction: Boolean)
