package com.onedev.mygooglemaps

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "GeofenceBroadcast"
        const val ACTION_GEOFENCE_EVENT = "GeofenceEvent"
        const val CHANNEL_ID = "1"
        private const val CHANNEL_NAME = "Geofence Channel"
        private const val NOTIFICATION_ID = 1
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_GEOFENCE_EVENT) {
            val geofencingEvent = GeofencingEvent.fromIntent(intent)

            if (geofencingEvent.hasError()) {
                val errorMessage =
                    GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
                sendNotification(context, errorMessage)
                Log.e(TAG, "onReceive: $errorMessage")
                return
            }

            val geofencingTransition = geofencingEvent.geofenceTransition
            if (geofencingTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofencingTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
                val geofenceTransitionString =
                    when (geofencingTransition) {
                        Geofence.GEOFENCE_TRANSITION_ENTER -> "Anda Memasuki Area"
                        Geofence.GEOFENCE_TRANSITION_DWELL -> "Anda Telah Didalam Area"
                        else -> "Invalid Transition Type"
                    }

                val triggeringGeofence = geofencingEvent.triggeringGeofences
                val requestId = triggeringGeofence[0].requestId

                val geofenceTransitionDetails = "$geofenceTransitionString $requestId"
                sendNotification(context, geofenceTransitionDetails)

                Log.i(TAG, "onReceive: $geofenceTransitionDetails")
            } else {
                val errorMessage = "Invalid transition type : $geofencingTransition"
                sendNotification(context, errorMessage)

                Log.e(TAG, "onReceive: $errorMessage")
            }
        }
    }

    private fun sendNotification(context: Context, errorMessage: String) {
        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(errorMessage)
            .setContentText("Anda sudah bisa absen sekarang :)")
            .setSmallIcon(R.drawable.ic_baseline_notifications_active)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            mBuilder.setChannelId(CHANNEL_ID)
            mNotificationManager.createNotificationChannel(channel)
        }
        val notification = mBuilder.build()
        mNotificationManager.notify(NOTIFICATION_ID, notification)
    }
}