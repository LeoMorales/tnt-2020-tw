package unpsjb.ing.tnt.mapsapp

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import unpsjb.ing.tnt.mapsapp.MapsActivity.Companion.ACCION_EVENTO_GEOFENCE

/*
 * Triggered by the Geofence.  Since we only have one active Geofence at once, we pull the request
 * ID from the first Geofence, and locate it within the registered landmark data in our
 * GeofencingConstants within GeofenceUtils, which is a linear string search. If we had  very large
 * numbers of Geofence possibilities, it might make sense to use a different data structure.  We
 * then pass the Geofence index into the notification, which allows us to have a custom "found"
 * message associated with each Geofence.
 * Activado por Geofence.
 * Como solo tenemos una Geofence activa a la vez, extraemos el ID de solicitud de la primer
 * Geofence y la ubicamos dentro de los datos de marcadores registrados en nuestros
 * GeofencingConstants dentro de GeofenceUtils, que es una búsqueda de cadena lineal.
 *  Si tuviéramos un gran número de posibilidades de Geofence, podría tener sentido
 *  usar una estructura de datos diferente.
 *  Luego pasamos el índice Geofence a la notificación, lo que nos permite tener un
 *  mensaje "encontrado" personalizado asociado con cada Geofence.
 */
class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACCION_EVENTO_GEOFENCE) {
            val eventoGeofencing = GeofencingEvent.fromIntent(intent)

            if (eventoGeofencing.hasError()) {
                val mensajeError = mensajeError(context, eventoGeofencing.errorCode)
                Log.e(TAG, mensajeError)
                return
            }

            if (eventoGeofencing.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                Log.v(TAG, context.getString(R.string.geofence_entrada))

                val geofenceId = when {
                    eventoGeofencing.triggeringGeofences.isNotEmpty() ->
                        eventoGeofencing.triggeringGeofences[0].requestId
                    else -> {
                        Log.e(TAG, "No se encontro disparador del Geofence! Salir")
                        return
                    }
                }
                // Comparar geofence con la lista de constantes en GeofenceUtil.kt
                // para ver si el usuario entró en alguna de las ubicaciones que
                // siguen nuestras geofences.
                val indexEncontrado = GeofencingConstants.MARCADORES_DATA.indexOfFirst {
                    it.id == geofenceId
                }

                // Geofences desconocidas son ignoradas
                if ( -1 == indexEncontrado ) {
                    Log.e(TAG, "Geofence desconocida: Salir")
                    return
                }

                val notificationManager = ContextCompat.getSystemService(
                    context,
                    NotificationManager::class.java
                ) as NotificationManager

                notificationManager.dispararNotificacionEntradaAlGeofence(
                    context, indexEncontrado
                )
            }
        }
    }
}

private const val TAG = "GeofenceReceiver"
