package unpsjb.ing.tnt.notificaciones

/*
 * Este código viene del codelab:
 * https://codelabs.developers.google.com/codelabs/advanced-android-kotlin-training-notifications/#3
 *
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat

// Notification ID.
private val NOTIFICATION_ID = 0

/**
 * Construye y entrega una notificacion.
 *
 * @param mensaje, texto de la notificacion.
 * @param context, activity contexto.
 */
fun NotificationManager.sendNotification(mensaje: String, applicationContext: Context) {
    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val unpsjbImagen = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.unpsjb
    )
    val imgGrande = NotificationCompat.BigPictureStyle()
        .bigPicture(unpsjbImagen)
        .bigLargeIcon(null)

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.pomodoro_notification_channel_id)
    )
        .setSmallIcon(R.drawable.ic_notificacion)
        .setContentTitle(applicationContext
            .getString(R.string.titulo_notificacion))
        .setContentText(mensaje)
        .setStyle(imgGrande)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setLargeIcon(unpsjbImagen)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(NOTIFICATION_ID, builder.build())
}

/**
 * Cancela todas las notificaciones.
 *
 */
fun NotificationManager.cancelarNotificaciones() {
    cancelAll()
}
