package unpsjb.ing.tnt.notificaciones

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import unpsjb.ing.tnt.notificaciones.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        crearCanal(
                getString(R.string.pomodoro_notification_channel_id),
                getString(R.string.pomodoro_notification_channel_name)
        )

        binding.botonLanzarNotificacion.setOnClickListener {
            notificar("Notificacion de Pomodoro!")
        }

    }

    private fun crearCanal(idCanal: String, nombreCanal: String) {

        // Crear canal de notificacion para versiones superiores a API 26.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canalNotificacion = NotificationChannel(
                    idCanal,
                    nombreCanal,
                    NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(false)
                    enableLights(true)
                    lightColor = Color.RED
                    enableVibration(true)
                    description = getString(R.string.pomodoro_notification_channel_description)
                }

            // Registrar el canal
            val notificationManager = getSystemService(
                    NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(canalNotificacion)

        }

    }

    private fun notificar(mensaje: String){
        val notificationManager = getSystemService(
            NotificationManager::class.java
        )
        notificationManager.sendNotification(
            mensaje,
            this)
    }

}
