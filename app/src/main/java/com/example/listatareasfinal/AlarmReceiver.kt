package com.example.listatareasfinal

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

/**
 * Se hereda de BroadcastReceiver
 */
class AlarmaReceiver : BroadcastReceiver() {
    // Se establecen los valores del ID del canl y de la notifivación
    private val ID_CANAL = "ejemplo_canal_01"
    private val idNotificacion = 101

    /**
     * Método que sobreescribe la función onRecive
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        // Obtengo el sistema de notificaciones, que lo casteo como un NotificationManager
        val administradorNotificaciones = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Intent para reiniciar la aplicación cuando se haga clic en la notificación
        val intentReinicio = Intent(context, MainActivity::class.java)
        val pendingIntentReinicio = PendingIntent.getActivity(context, 0, intentReinicio,
            PendingIntent.FLAG_IMMUTABLE)

        // Creo una notificación con el método Builder, pasando el contexto y aseguro que este no
        // será nulo, le paso además el ID del canl
        // Le paso los parámetros para crearla.
        val notificacion = NotificationCompat.Builder(context!!, ID_CANAL)
            .setContentTitle("Alarma Activada")
            .setContentText("Tu alarma programada se ha activado.")
            .setSmallIcon(R.drawable.ic_notificacion)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntentReinicio)  // Se añade el PendingIntent a la notificación
            .setAutoCancel(true)  // La notificación se cierra automáticamente al hacer clic en ella
            .build()

        //Muestra la notificación
        administradorNotificaciones.notify(idNotificacion, notificacion)
    }
}