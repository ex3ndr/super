package com.superapp.audio

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.facebook.react.HeadlessJsTaskService
import com.facebook.react.bridge.Arguments
import com.facebook.react.jstasks.HeadlessJsTaskConfig
import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

class AudioForegroundServiceOptions : Record {
    @Field
    val headlessTaskName: String = "default"

    @Field
    val notificationTitle: String = "Notification Title"

    @Field
    val notificationDesc: String = "Notification Description"

    @Field
    val notificationColor: String = "#FFC107"

    @Field
    val notificationIconName: String = "ic_launcher"

    @Field
    val notificationIconType: String = "mipmap"

    @Field
    val notificationProgress: Int = 0

    @Field
    val notificationMaxProgress: Int = 100

    @Field
    val notificationIndeterminate: Boolean = false

    @Field
    val linkingURI: String = ""
}

class AudioForegroundService : HeadlessJsTaskService() {
    companion object {
        private const val CHANNEL_ID = "ExpoForegroundActionChannel"
        fun buildNotification(
                context: Context,
                notificationTitle: String,
                notificationDesc: String,
                notificationColor: Int,
                notificationIconInt: Int,
                notificationProgress: Int,
                notificationMaxProgress: Int,
                notificationIndeterminate: Boolean,
                linkingURI: String
        ): Notification {
            val notificationIntent: Intent = if (linkingURI.isNotEmpty()) {
                Intent(Intent.ACTION_VIEW, Uri.parse(linkingURI))
            } else {
                Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
            }
            val contentIntent: PendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationDesc)
                    .setSmallIcon(notificationIconInt)
                    .setContentIntent(contentIntent)
                    .setOngoing(true)
                    .setSilent(true)
                    .setProgress(notificationMaxProgress, notificationProgress, notificationIndeterminate)
                    .setPriority(NotificationCompat.PRIORITY_MIN)
                    .setColor(notificationColor)
            return builder.build()
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("AUDIO_FOREGROUND", "onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("AUDIO_FOREGROUND", "onDestroy")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val extras: Bundle? = intent?.extras
        requireNotNull(extras) { "Extras cannot be null" }


        val notificationTitle: String = extras.getString("notificationTitle")!!;
        val notificationDesc: String = extras.getString("notificationDesc")!!;
        val notificationColor: Int = Color.parseColor(extras.getString("notificationColor"))
        val notificationIconInt: Int = extras.getInt("notificationIconInt");
        val notificationProgress: Int = extras.getInt("notificationProgress");
        val notificationMaxProgress: Int = extras.getInt("notificationMaxProgress");
        val notificationIndeterminate: Boolean = extras.getBoolean("notificationIndeterminate");
        val notificationId: Int = extras.getInt("notificationId");
        val linkingURI: String = extras.getString("linkingURI")!!;


        println("notificationIconInt");
        println(notificationIconInt);
        println("On create door dion")
        println("onStartCommand")
        createNotificationChannel() // Necessary creating channel for API 26+
        println("After createNotificationChannel")

        println("buildNotification")
        val notification: Notification = buildNotification(
                this,
                notificationTitle,
                notificationDesc,
                notificationColor,
                notificationIconInt,
                notificationProgress,
                notificationMaxProgress,
                notificationIndeterminate,
                linkingURI
        )
        println("Starting foreground")

        startForeground(notificationId, notification)
        println("After foreground")
        return super.onStartCommand(intent, flags, startId)
    }

    private fun createNotificationChannel() {
        println("createNotificationChannel")
        val serviceChannel = NotificationChannel(CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT)
        val manager = getSystemService(NotificationManager::class.java)
        manager!!.createNotificationChannel(serviceChannel)
    }

    override fun getTaskConfig(intent: Intent?): HeadlessJsTaskConfig? {
        return intent?.extras?.let {
            HeadlessJsTaskConfig(
                    it.getString("headlessTaskName")!!,
                    Arguments.fromBundle(it),
                    0,
                    true
            )
        }
    }
}