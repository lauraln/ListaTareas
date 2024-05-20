package com.example.listatareasfinal

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar


//IMPORTANTE AÑADIR LA ACTIVIDAD NUEVA EN AndroidManifest

class ThirdActivity : AppCompatActivity() {

    //Inicializada vacía
    private var tasksCompleted =mutableListOf<Task>()
    private lateinit var rvCompletedTasks: RecyclerView
    private lateinit var adapter:TaskCompletedAdapter
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var dbHandler: TasksBD

    // Definimos constantes para el ID del canal de notificaciones y el ID de la notificación.
    private val ID_CANAL = "canal_notificaciones_01"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        // Inicializa la instancia de la base de datos usando 'this' como contexto
        dbHandler = TasksBD(this)
        initRecyclerView()
        recogerParametrosSecondActivity()

        // Encontramos el botón en el layout y establecemos un escuchador de clics
        val botonEstablecerAlarma: Button = findViewById(R.id.btnNoti)
        botonEstablecerAlarma.setOnClickListener {
            // Al hacer clic en el botón, se abre el diálogo para seleccionar la hora
            abrirTimePickerDialog()
        }

    }

    private fun initRecyclerView() {
        /*tasksCompleted = mutableListOf(
            Task(1,"Comprar leche", "2024-05-10 10:00", 0),
            Task(2,"Enviar correo", "2024-05-10 12:00", 0)
        )*/
        getCompleteTasks()
        rvCompletedTasks = findViewById(R.id.rvCompletedTasks)
        rvCompletedTasks.layoutManager = LinearLayoutManager(this)
        adapter = TaskCompletedAdapter(tasksCompleted, {position -> updateCompleteTask(position)})
        rvCompletedTasks.adapter=adapter
        accionVolver()
    }

    private fun updateCompleteTask(position:Int){
        //Debemos llamar a Activity 2 dejando solo modificar completado a no completado

        Log.d("Antes de salir de Activity 3 rumbo a Activity 2", "posición: ${position}")
        val task = tasksCompleted[position]
        val intent = Intent(this@ThirdActivity, SecondActivity::class.java).apply {
            putExtra("taskTitle", task.title)
            putExtra("taskDateTime", task.dateTime)
            putExtra("taskCompleted", task.isCompleted)
            putExtra("position", position)
            putExtra("id", task.id)
            putExtra("origen", "ThirdActivity")
            Log.d("Salgo de Activity 3", "posición: ${position}")
        }
        resultLauncher.launch(intent)
    }

    private fun recogerParametrosSecondActivity() {
        //RECOGEMOS LOS VALORES AL VOLVER DE LA ACTIVITY2
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val title = data?.getStringExtra("title")
                    val date = data?.getStringExtra("date")
                    val completed = data?.getIntExtra("completed", 0)
                    val id = data?.getLongExtra("id", -1)
                    val position = data?.getIntExtra("position", -1)

                    Log.d("Vuelvo a ActivityThird", "c")
                    Log.d("Vuelvo a ActivityThird", "Tarea: ${title}")
                    Log.d("Vuelvo a ActivityThird", "Tarea: ${date}")
                    Log.d("Vuelvo a ActivityThird", "Tarea: ${completed}")

                    //Eliminar tarea de la lista que que ha pasado a no completada
                    var task = Task(id!!, title!!, date!!, completed!!)
                    //addTask(task)
                    //Actualizar en la base de datos
                    //Actualizar lista de completadas Actividad 1¿?

                    //En cualquier caso actualizo la base de datos
                    dbHandler.updateTask(task)
                    //Elimino de la lista en caso de que sea marcada como no completada
                    if(completed==0) {
                        if (position != null) {
                            tasksCompleted.removeAt(position)
                            adapter?.notifyDataSetChanged()
                        }

                    }
                }
            }

    }

     // Método para recuperar todas las tareas incompletas
    private fun getCompleteTasks() {
        val cursor = dbHandler.getCompletedTasks()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"))
            val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
            val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
            val completed = cursor.getInt(cursor.getColumnIndexOrThrow("completed"))
            tasksCompleted.add(Task(id,title,date,completed))
        }
        cursor.close()
    }

    private fun accionVolver(){
        val btnBack: Button = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this@ThirdActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    // Método para abrir un TimePickerDialog y permitir al usuario seleccionar una hora
    private fun abrirTimePickerDialog() {
        // Obtengo una instancia del calendario y fijo la hora y minutos actuales
        val calendario = Calendar.getInstance()
        val hora = calendario.get(Calendar.HOUR_OF_DAY)
        val minutos = calendario.get(Calendar.MINUTE)

        // Creo un timePickerDialog y le paso el contexto, la hora seleccionada y el minuto
        // y establezco una alarma
        val timePickerDialog = TimePickerDialog(this,
            TimePickerDialog.OnTimeSetListener { _, horaSeleccionada, minutoSeleccionado ->
                // Este código se ejecuta cuando el usuario selecciona una hora
                establecerAlarma(horaSeleccionada, minutoSeleccionado)
            }, hora, minutos, true)

        timePickerDialog.show()
    }

    // Método para configurar una alarma a la hora especificada por el usuario
    private fun establecerAlarma(hora: Int, minutos: Int) {
        // Obtengo una instancia del calendario y le fijo la hora y minutos obtenidos por parámetro
        val calendarioAlarma = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hora)
            set(Calendar.MINUTE, minutos)
            set(Calendar.SECOND, 0)
        }

        // Obtengo una instancia del servicio ALARM_SERVICE de tipo AlarmManager
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // Creo un intent que paso a la clase AlarmReceiver que hereda de BroadcastReceiver
        val intent = Intent(this, AlarmaReceiver::class.java)
        // Declaro un pendingIntent, que es un tipo de intent que queda pendiente de que alguien
        // lo ejecute, será el AlarmManager quien lo ejecute
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Configuramos la alarma en el tiempo especificado y ejecutará el pendintIntent
        // que en este caso crea la notificación
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarioAlarma.timeInMillis, pendingIntent)
    }
}