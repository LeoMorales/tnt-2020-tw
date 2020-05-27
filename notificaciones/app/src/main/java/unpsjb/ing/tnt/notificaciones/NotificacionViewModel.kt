package unpsjb.ing.tnt.notificaciones

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel

class NotificacionViewModel(application: Application) : AndroidViewModel(application) {

    private fun notificar(){
        val notificationManager =
            ContextCompat.getSystemService(
                getApplication(),
                NotificationManager::class.java
            ) as NotificationManager
        notificationManager.sendNotification(
            "mensaje",
            getApplication())
    }

}