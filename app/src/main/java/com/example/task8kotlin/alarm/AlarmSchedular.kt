package com.example.task8kotlin.alarm
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import com.example.task8kotlin.R
import com.example.task8kotlin.roomdb.DataModel
import java.time.ZoneId
import java.util.Calendar
class AlarmSchedular(private val context: Context): AlarmServiceManager {
private val alarmService = context.getSystemService(AlarmManager::class.java)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun scheduleAlarm(data: DataModel) {
        val calender=Calendar.getInstance()
        calender.set(data.time.year,data.time.monthValue,data.time.dayOfMonth,data.time.hour,data.time.minute)
    var intent= Intent(context,AlarmReciever::class.java).apply {
        putExtra(context.getString(R.string.intentKey),""+data.id+":"+data.time)
    }
        if (VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            alarmService.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                calender.timeInMillis,
                PendingIntent.getBroadcast(context,data.hashCode(),intent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE))
        } else {
        }
    }
    override fun cancelAlarm(data: DataModel) {
    }
}