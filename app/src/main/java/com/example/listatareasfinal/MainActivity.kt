package com.example.listatareasfinal

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView

//Web para los iconos https://pictogrammers.com/library/mdi/icon/check-all/
class MainActivity : AppCompatActivity(), Fragment1.OnTaskInteractionListener  {

    private lateinit var btnAddTask: Button
    private lateinit var etTask: EditText
    private lateinit var rvTasks: RecyclerView





    private var tasks =mutableListOf<Task>()
    private var currentTask: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //initUi()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)



        //Cargamos los dos fragmentos
        if (savedInstanceState == null) {
            // Carga el Fragment A
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container_1, Fragment1())
                .commit()

            // Carga el Fragment B
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container_2, Fragment2())
                .commit()
        }
        //RECOGEMOS LOS VALORES AL VOLVER DE LA ACTIVITY2
        var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val title = data?.getStringExtra("title")
                val date = data?.getStringExtra("date")
                val completed = data?.getIntExtra("completed",0)

                Log.d("Vuelvo a Activity MAin1","c")
                Log.d("Vuelvo a Activity MAin2", "Tarea: ${title}")
                Log.d("Vuelvo a Activity MAin2", "Tarea: ${date}")
                Log.d("Vuelvo a Activity MAin2", "Tarea: ${completed}")

                var task = Task(null,title!!,date!!,completed!!)
                addTask(task)
            }
        }
        //El botón + que me lleva a la Actividad 2
        val btnNewActivity: Button = findViewById(R.id.btnNewActivity)
        btnNewActivity.setOnClickListener {
            val intent = Intent(this@MainActivity, SecondActivity::class.java)
            resultLauncher.launch(intent)
        }

        //El botón Tareas Completadas que me lleva a la Actividad 3
        val btnNewActivity3: Button = findViewById(R.id.btnCompletedTasks)
        btnNewActivity3.setOnClickListener {
            val intent = Intent(this@MainActivity, ThirdActivity::class.java)
            startActivity(intent)
            finish()
        }

        val sharedPreferences = getSharedPreferences("FontPreferences", Context.MODE_PRIVATE)
        val savedFontSize = sharedPreferences.getString("FontSize", "default")
        updateTextSizes(savedFontSize!!)



    }

    fun addTask(task: Task) {
        val fragment1 = supportFragmentManager.findFragmentById(R.id.fragment_container_1) as? Fragment1
        fragment1?.addTask(task)
    }
    override fun onTaskSelected(task: Task) {
        val fragment2 = supportFragmentManager.findFragmentById(R.id.fragment_container_2) as? Fragment2
        fragment2?.updateContent(task)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menufuente, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.font_small -> {
                setFontSize("small")
                return true
            }
            R.id.font_large -> {
                setFontSize("large")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun setFontSize(size: String) {
        val sharedPreferences = getSharedPreferences("FontPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("FontSize", size)
        editor.apply()

        updateTextSizes(size)
    }

    fun updateTextSizes(size: String) {
        val textSize = when (size) {
            "small" -> 14f  // Tamaño de fuente pequeño
            "large" -> 18f  // Tamaño de fuente grande
            else -> 16f     // Default
        }


        findViewById<TextView>(R.id.textView1).textSize = textSize
        // Actualiza cualquier otro TextView aquí
    }



}