package com.example.task8kotlin.roomdb

import android.app.Application
import android.os.Build
import androidx.lifecycle.LiveData
import java.time.LocalDateTime

class AppRepository(application: Application) {
    var application:Application
    init {
        this.application=application
    }
    val getLiveData : LiveData<List<TimeStringDataModel>> = AppDatabase.getInstance(application).usersDao().getDataModel()
    suspend fun insert(dataModel: DataModel){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            var timeStringDataModel=TimeStringDataModel(id=dataModel.id,time=LocalDateTime.parse(dataModel.time.toString()).toString(), delAction = dataModel.delAction)

            AppDatabase.getInstance(application.applicationContext).usersDao().insert(timeStringDataModel)
        }
    }

   suspend fun delete(stringdataModel: TimeStringDataModel){
        AppDatabase.getInstance(application.applicationContext).usersDao().delete(stringdataModel)
    }
    suspend fun deleteByTimeValue(time : String){
        AppDatabase.getInstance(application.applicationContext).usersDao().deleteByStringValue(time)
    }
    suspend fun deleteAllEntries(){
        AppDatabase.getInstance(application.applicationContext).usersDao().deleteAllRows()

    }
    suspend fun UpdateSingleObject(data: TimeStringDataModel){
        AppDatabase.getInstance(application.applicationContext).usersDao().Update(data)
    }

}