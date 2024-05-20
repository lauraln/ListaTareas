package com.example.listatareasfinal

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Fragment1: Fragment(){

    private var tasks =mutableListOf<Task>()
    private lateinit var rvTasks: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var dbHandler: TasksBD


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializa la instancia de la base de datos
        context?.let {
            dbHandler = TasksBD(it)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_1, container, false)

        //Datos de prueba
        /*tasks = mutableListOf(
            Task("Comprar leche", "2024-05-10 10:00", false),
            Task("Enviar correo", "2024-05-10 12:00", false)
        )*/

        getInCompleteTasks()

        Log.d("Fragment1", "Número de tareas: ${tasks.size}")
        // Inicializa RecyclerView
        rvTasks = view.findViewById(R.id.rvTasks)
        rvTasks.layoutManager = LinearLayoutManager(context)
        taskAdapter = TaskAdapter(tasks){ task ->
            // Aquí puedes manejar el clic. Por ejemplo:
            (activity as? MainActivity)?.onTaskSelected(task)
        }
        rvTasks.adapter = taskAdapter

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("Vuelvo a Fragment1","c")
                val data: Intent? = result.data
                val title = data?.getStringExtra("title")
                val date = data?.getStringExtra("date")
                val completed = data?.getIntExtra("completed", 0)
                val position = data?.getIntExtra("position", -1)
                val id = data?.getLongExtra("id", -1)

                var task = Task(id, title!!, date!!, completed!!)

                Log.d("Vuelvo a Fragment1", "id: ${id}")
                Log.d("Vuelvo a Fragment1", "Tarea: ${title}")
                Log.d("Vuelvo a Fragment1", "Tarea: ${date}")
                Log.d("Vuelvo a Fragment1", "Tarea: ${completed}")
                Log.d("Vuelvo a Fragment1", "posición: ${position}")

                //En cualquier caso actualizo la base de datos
                updateTaskBD(task)
                //Elimino de la lista en caso de que sea completada
                //En otro caso solo actualizo los demás campos
                if(completed==0) {
                    updateTaskList(Task(id, title!!, date!!, completed!!), position!!)
                }
                else{
                    deleteTaskList(position!!)
                }
                taskAdapter?.notifyDataSetChanged()

            }
        }



        return view
    }

    private fun updateTaskList(task: Task, position:Int) {

        updateTaskBD(task)
        updateTask(task,position)
    }

    private fun updateTaskBD(task: Task) {

        dbHandler.updateTask(task)

    }

    private fun updateTask(task: Task, position:Int) {

        if (position >= 0 && position < tasks.size) {
            tasks[position] = task
            taskAdapter?.notifyDataSetChanged()
        }

    }


    interface OnTaskInteractionListener {
        fun onTaskSelected(task: Task)
    }

    private var listener: OnTaskInteractionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnTaskInteractionListener
            ?: throw RuntimeException("$context must implement OnTaskInteractionListener")
    }

    fun someFunctionToHandleClick(task: Task) {
        listener?.onTaskSelected(task)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = item.groupId // La posición se puede almacenar en groupId
        when (item.itemId) {
            R.id.ToDoTask -> {
                completeTask(position)
                return true
            }
            R.id.Deletetask -> {
                deleteTask(position)
                return true
            }
            R.id.Updatetask -> {
                Log.d("Entro en switch","update")
                updateTask(position)
                return true
            }
            R.id.Sendtask -> {
                sendTask(position)
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun sendTask(position: Int) {
        if (position < 0 || position >= tasks.size) return

        val task = tasks[position]
        val subject = "Detalles de la Tarea: ${task.title}"
        val message = buildString {
            append("Título: ${task.title}\n")
            append("Fecha y Hora: ${task.dateTime}\n")
            append("Completada: ${if (task.isCompleted == 1) "Sí" else "No"}")
        }

// Definir el destinatario del correo electrónico
        val recipient = arrayOf("lauraln80@gmail.com")  // Reemplaza esto con la dirección de correo real

// Crear Intent para enviar correo
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, recipient)  // Establecer el destinatario del correo
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
        }

// Intentar iniciar la actividad con un chooser
        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar tarea por correo..."))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(requireContext(), "No hay clientes de correo instalados.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateTask(position: Int) {
        Log.d("Entro","update")
        Log.d("Antes de salir de Fra1", "posición: ${position}")
            val task = tasks[position]
            val intent = Intent(activity, SecondActivity::class.java).apply {
                putExtra("taskTitle", task.title)
                putExtra("taskDateTime", task.dateTime)
                putExtra("taskCompleted", task.isCompleted)
                putExtra("position", position)
                putExtra("id", task.id)
                Log.d("Salgo de Fragment1", "posición: ${position}")
            }
            resultLauncher.launch(intent)

    }

    fun completeTask(position: Int) {

       // dbHandler.updateTask(tasks[position])
        // Código para completar la tarea en la posición 'position'
        if (position >= 0 && position < tasks.size) {
            val task = tasks[position]
            // Cambiar el estado de la tarea a completado
            if (task.isCompleted!=1) {
                task.isCompleted = 1
                tasks[position]=task
                dbHandler.updateTask(tasks[position])
                deleteTaskList(position)
                // Notificar al adaptador que el ítem ha cambiado para actualizar la vista
                (rvTasks.adapter as? TaskAdapter)?.notifyItemChanged(position)
            }
        }
    }

    fun deleteTask(position: Int) {
        //Borro de la base de datos
        deleteTaskBD(tasks[position].id!!)
        //Borro de la lista
        deleteTaskList(position)
    }

    fun deleteTaskList(position: Int) {
        tasks.removeAt(position)
        taskAdapter?.notifyDataSetChanged()
    }

    fun deleteTaskBD(id: Long) {
        // Código para eliminar la tarea en la posición 'position'
        dbHandler.deleteTask(id)
    }


    /* Operaciones con la base de datos*/
    fun addTask(task:Task) {
        // Código para eliminar la tarea en la posición 'position'
        var taskId =dbHandler.addTask(task)
        task.id=taskId
        tasks.add(task)
        taskAdapter?.notifyDataSetChanged()
    }

    // Método para recuperar todas las tareas incompletas
    fun getInCompleteTasks() {
        val cursor = dbHandler.getInCompletedTasks()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"))
            val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
            val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
            val completed = cursor.getInt(cursor.getColumnIndexOrThrow("completed"))
            tasks.add(Task(id,title,date,completed))
        }
        cursor.close()
    }

}