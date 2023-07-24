package com.example.task8kotlin.main
import android.app.Application
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.task8kotlin.alarm.AlarmSchedular
import com.example.task8kotlin.roomdb.AppRepository
import com.example.task8kotlin.roomdb.DataModel
import com.example.task8kotlin.roomdb.TimeStringDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class MainViewModel(application: Application) : AndroidViewModel(application) {
     var context:Application
     lateinit var dataModelObject: DataModel
     var alarmschedular= AlarmSchedular(application.applicationContext)
     var appRepository: AppRepository = AppRepository(application)
     var dataModellist = ArrayList<DataModel>()
    lateinit var tenMinutesThreshold: LocalDateTime
    init {
        context=application
    }
    var liveDataRoom : LiveData<List<TimeStringDataModel>> = appRepository.getLiveData.map { it.reversed()}
    fun saveDataModelToRoom(dataModel: DataModel){
    viewModelScope.launch(Dispatchers.IO) {
    appRepository.insert(dataModel)
    }
    }
     fun deleteAllEntries(){
        viewModelScope.launch(Dispatchers.IO) {
        appRepository.deleteAllEntries()
        }
    }
    fun UpdateSingleObject(data: DataModel){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            viewModelScope.launch(Dispatchers.IO) {
                appRepository.UpdateSingleObject(
                    TimeStringDataModel(
                        id = data.id, time = LocalDateTime.parse(data.time.toString()).toString(),
                        delAction = data.delAction)) }
        }
    }
    fun deleteByTimeValue(time: String){
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.deleteByTimeValue(time)
        }
    }
}