package com.example.task8kotlin.alarm

import com.example.task8kotlin.roomdb.DataModel

interface AlarmServiceManager {
    fun scheduleAlarm(data: DataModel)
    fun cancelAlarm(data: DataModel)
}