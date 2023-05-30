package com.example.bt.recievers



import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.bt.*
import com.example.bt.BusDriverActivity
import com.example.bt.activities.BusDriverActivity
import com.example.bt.services.LocationUpdaterService

class DeviceStateListener : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            ConnectivityManager.CONNECTIVITY_ACTION -> {
                val state = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
                val info = state?.activeNetworkInfo
                if (info == null || !info.isConnected) showError(context)
            }
            LocationManager.PROVIDERS_CHANGED_ACTION -> {
                val manager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
                manager ?: return
                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    // all ok
                    System.err.println("Providers are : "+manager.allProviders)
                } else showError(context)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun showError(context: Context?) {
        // show a notification saying that the service has stopped due to provider was disabled.
        context ?: return
        val notification = NotificationCompat.Builder(context, CHANEL_ID)
            .setContentTitle(context.getString(R.string.service_stopped))
            .setContentIntent(PendingIntent.getActivity(context, 999,
                Intent(context, BusDriverActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    action = Intent.ACTION_MAIN
                }, 0))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setContentText(context.getString(R.string.turn_on_location))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        NotificationManagerCompat.from(context).apply {
            cancel(222)
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
            notify(999, notification.build())
        }
        Toast.makeText(context, R.string.turn_on_location, Toast.LENGTH_SHORT).show()
        stopService(context)
    }

    private fun stopService(context: Context) {
        val intent = Intent(context, LocationUpdaterService::class.java)
        intent.action = ACTION_STOP_FOREGROUND
        context.startService(intent)
    }
}
