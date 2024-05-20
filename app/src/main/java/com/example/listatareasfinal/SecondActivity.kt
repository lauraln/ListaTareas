package com.example.listatareasfinal

import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date


class SecondActivity : AppCompatActivity() {

    private lateinit var editTextTaskTitle: EditText
    private lateinit var editTextTaskDateTime: EditText
    private lateinit var checkBoxCompleted: CheckBox
    private var position: Int = -1
    private var id: Long = -1

    // Definimos constantes para el ID del canal de notificaciones y el ID de la notificación.
    private val ID_CANAL = "canal_notificaciones_01"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        editTextTaskTitle = findViewById(R.id.editTextTaskTitle)
        editTextTaskDateTime = findViewById(R.id.editTextTaskDateTime)
        checkBoxCompleted = findViewById(R.id.checkBoxCompleted)

        // Set initial task details
        editTextTaskTitle.setText(intent.getStringExtra("taskTitle"))
        editTextTaskDateTime.setText(intent.getStringExtra("taskDateTime"))

        if(intent.getStringExtra("origen") !=null){
            if(intent.getStringExtra("origen")=="ThirdActivity") {
                editTextTaskTitle.isEnabled = false
                editTextTaskDateTime.isEnabled = false
            }
        }

        val isCompleted=intent.getIntExtra("taskCompleted",-1)
        checkBoxCompleted.isChecked = isCompleted==1
        position = intent.getIntExtra("position",-1)
        if( position == -1){
            checkBoxCompleted.isEnabled = false
        }
        id=intent.getLongExtra("id",-1)

        Log.d("En SecondActivity", "posición: ${position}")
        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            saveChanges()
        }

        findViewById<Button>(R.id.buttonClear).setOnClickListener {
            clearFields()
        }
    }

    private fun saveChanges() {
        // Logic to save changes to the task
        // This could involve sending data back to the main activity or updating a database

        val intent = Intent()
        //var task:Task = Task(editTextTaskTitle.text.toString(),editTextTaskDateTime.text.toString(),checkBoxCompleted.isChecked )

        intent.putExtra("title", editTextTaskTitle.text.toString())
        intent.putExtra("date", editTextTaskDateTime.text.toString())
        if(checkBoxCompleted.isChecked){
            intent.putExtra("completed", 1)
        }
       else{
            intent.putExtra("completed", 0)
            //createChannel(this)
           // establecerAlarma(editTextTaskDateTime.text.toString(),editTextTaskTitle.text.toString())
        }
        Log.d("En saveChanges", "posición: ${position}")
        intent.putExtra("position", position)
        intent.putExtra("id", id)
        setResult(Activity.RESULT_OK, intent)
        finish()


    }

    private fun clearFields() {
        // Clear all fields in the activity
        editTextTaskTitle.setText("")
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        val date = current.format(formatter)
        editTextTaskDateTime.setText(date)
        checkBoxCompleted.isChecked = false
    }

    // Método para configurar una alarma a la hora especificada por el usuario
    private fun establecerAlarma(date:String,texto:String) {
        //Tengo la fecha en string la convierto para poder pasarsela a la alarma
        val formato = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val fechaHora: Date = formato.parse(date)

        // Obtengo una instancia del calendario y le fijo la hora y minutos obtenidos por parámetro
        val calendarioAlarma = Calendar.getInstance();
        calendarioAlarma.setTime(fechaHora);  // Establece el objeto Calendar con la fecha y hora parseada

        // Obtengo una instancia del servicio ALARM_SERVICE de tipo AlarmManager
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // Creo un intent que paso a la clase AlarmReceiver que hereda de BroadcastReceiver
        val intent = Intent(applicationContext, AlarmaReceiver_old::class.java)
        intent.putExtra("contenido", texto)
        // Declaro un pendingIntent, que es un tipo de intent que queda pendiente de que alguien
        // lo ejecute, será el AlarmManager quien lo ejecute
        val pendingIntent = PendingIntent.getBroadcast(applicationContext, 101, intent, PendingIntent.FLAG_IMMUTABLE)

        // Configuramos la alarma en el tiempo especificado y ejecutará el pendintIntent
        // que en este caso crea la notificación

        //createChannel(this)
       // alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarioAlarma.timeInMillis, pendingIntent)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, Calendar.getInstance().timeInMillis+1500, pendingIntent)
    }


    fun createChannel(context: Context?){
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                ID_CANAL,
                "mySuperChannel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply{
                description= "LOGS"
            }

            context?.let {
                val notificationManager: NotificationManager =
                    it.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                notificationManager.createNotificationChannel(channel)
            }
        }
    }

}
