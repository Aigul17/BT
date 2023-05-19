package com.example.bt

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat.EXTRA_NOTIFICATION_ID
import androidx.core.app.NotificationManagerCompat

class NotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val id = intent?.getIntExtra(EXTRA_NOTIFICATION_ID, 0)
        if (id != null && id > 0) {
            val manager: NotificationManagerCompat = NotificationManagerCompat.from(context!!)
            manager.cancel(id)
        }
    }
}