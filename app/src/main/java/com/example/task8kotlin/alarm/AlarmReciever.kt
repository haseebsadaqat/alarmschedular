package com.example.task8kotlin.alarm
import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.provider.Settings
import android.provider.Settings.Secure.SETTINGS_CLASSNAME
import android.provider.Settings.Secure.getString
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.task8kotlin.R
import com.example.task8kotlin.main.RecyclerAdapter
import com.example.task8kotlin.roomdb.AppDatabase
import com.example.task8kotlin.roomdb.AppRepository
import com.example.task8kotlin.roomdb.TimeStringDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
class AlarmReciever : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
       val idWithTime=intent?.getStringExtra(context.getString(R.string.intentKey)) ?: return
        CoroutineScope(Dispatchers.IO).launch {
            AppDatabase.getInstance(context).usersDao().updateBooleanValueByStringValue(idWithTime.substring(2),true)
        }
        showNotification(context,context.getString(R.string.time),idWithTime)

    }
    @SuppressLint("MissingPermission")
    private fun showNotification(context: Context, title: String?, body: String?) {
        val channelId = context.getString(R.string.myChanneId)
        val notificationId = 1
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
        val notificationManager = NotificationManagerCompat.from(context)
        // Create a notification channel for Android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                context.getString(R.string.myChannel),
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableLights(true)
            channel.lightColor = Color.RED
            notificationManager.createNotificationChannel(channel)
        }
        // Show the notification
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}