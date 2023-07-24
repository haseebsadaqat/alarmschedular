package com.example.task8kotlin.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.task7kotlin.utils.CommonKeys
@Database(entities = [TimeStringDataModel::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
abstract fun usersDao(): Dao
companion object{
private var instance : AppDatabase? = null
    @Synchronized
    fun getInstance(ctx: Context) : AppDatabase{
        if(instance == null){
        instance = Room.databaseBuilder( ctx.applicationContext, AppDatabase::class.java ,CommonKeys.Db_Name)
        .fallbackToDestructiveMigration().build()
    }
    return instance as AppDatabase
}
}
}