package com.example.bt.adapters

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.example.bt.CHANEL_ID
import com.example.bt.EXTRA_NOTIFICATION_ID
import com.example.bt.R
import com.example.bt.activities.HomeActivity
import com.example.bt.recievers.NotificationActionReceiver
import java.util.*
import kotlin.math.abs

class OnlineBusAdapter(private val activity: HomeActivity, options: FirestoreRecyclerOptions<Any>, private val listener: (item: Map<*, *>) -> Unit) : AnyAdapter(options, listener) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val bus = getItem(position) as Map<*, *>
        holder.view.apply {
            findViewById<TextView>(R.id.textName).text = bus["name"]?.toString()
            findViewById<TextView>(R.id.text2).text = bus["location"]?.toString()
            setOnClickListener { listener.invoke(bus) }
        }

        // Check message
        val time = bus["msg_time"] as Date?
        val msgTime = time?.toString() ?: "NA"
        val msg = bus["message"] as String? ?: ""

        val lastReceived = activity.sharedPreferences.getString(bus["uid"] as String? ?: "NA", "")
        if (lastReceived != msgTime) {
            // Keep this time saved
            val editor = activity.sharedPreferences.edit()
            editor.putString(bus["uid"] as String? ?: "NA", msgTime)
            editor.apply()

            if (msg.isBlank()) return
            // Show message notification
            val i = Intent(activity, NotificationActionReceiver::class.java)
            val nid = abs(((bus["uid"]?.hashCode()?.dec() ?: 1) % 100) + 1)
//            System.out.println("Here are nid $nid")
            i.putExtra(EXTRA_NOTIFICATION_ID, nid)
            val builder = NotificationCompat.Builder(activity, CHANEL_ID)
//                    .setContentIntent(PendingIntent.getBroadcast(activity, nid, i, 0))
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle(bus["name"]?.toString())
                .setContentText(msg)
                .setStyle(NotificationCompat.BigTextStyle().bigText(msg))
                .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .addAction(android.R.drawable.ic_notification_clear_all, activity.getString(R.string.dismiss),
                    PendingIntent.getBroadcast(activity, nid, i, PendingIntent.FLAG_IMMUTABLE))
            if (time != null) {
                builder.setShowWhen(true)
                builder.setWhen(time.time)
            }
            val manager: NotificationManagerCompat = NotificationManagerCompat.from(activity)

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            manager.notify(nid, builder.build())
        }
    }
}
