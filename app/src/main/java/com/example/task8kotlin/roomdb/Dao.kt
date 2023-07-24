package com.example.task8kotlin.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*

@androidx.room.Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stringdataModel: TimeStringDataModel)

    @Query("SELECT * FROM users")
    fun getDataModel() : LiveData<List<TimeStringDataModel>>

    @Delete
   suspend fun delete(stringdataModel: TimeStringDataModel)

    @Query("DELETE FROM users WHERE time = :stringValue")
    suspend fun deleteByStringValue(stringValue: String)

    @Query("UPDATE users SET delAction = :delValue WHERE time = :timeStringValue")
    suspend fun updateBooleanValueByStringValue(timeStringValue: String, delValue: Boolean)

    @Query("DELETE FROM users")
    suspend fun deleteAllRows()

    @Update
    suspend fun Update(stringdataModel: TimeStringDataModel)

}