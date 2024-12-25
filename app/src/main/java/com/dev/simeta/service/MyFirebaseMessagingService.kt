package com.dev.simeta.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dev.simeta.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Extract custom data from the message payload
        val data = remoteMessage.data
        val milestoneId = data["milestoneId"]
        val milestoneName = data["milestoneName"] ?: "Milestone Update"
        val status = data["status"] ?: "COMPLETED"
        val completionDate = data["completionDate"] ?: "N/A"

        // Log data for debugging
        Log.d(TAG, "Milestone ID: $milestoneId")
        Log.d(TAG, "Milestone Name: $milestoneName")
        Log.d(TAG, "Status: $status")
        Log.d(TAG, "Completion Date: $completionDate")

        // Build and show the notification
        val notificationMessage = "Milestone \"$milestoneName\" has been $status on $completionDate"
        showNotification(
            title = milestoneName,
            message = notificationMessage
        )
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "milestone_channel"
        val channelName = "Milestone Updates"

        // Create NotificationChannel for Android 8.0 and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = "Notifications about milestone updates"
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Check notification permission (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "Notification permission not granted. Notification not sent.")
                return
            }
        }

        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.icon_bimbingan)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        // Show the notification
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notificationBuilder.build())

        // Log notification details
        Log.d(TAG, "Notification sent: Title='$title', Message='$message'")
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        sendTokenToServer(token)
    }

    private fun sendTokenToServer(token: String) {
        try {
            // Example logic for sending the token to your backend server
            // Assume you have a Repository or API service to handle this
            Log.d(TAG, "Sending FCM token to server: $token")
            // AuthRepository.updateFcmToken(token) // Example function
        } catch (e: Exception) {
            Log.e(TAG, "Failed to send token to server: ${e.message}")
        }
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
        private const val NOTIFICATION_ID = 1
    }
}
