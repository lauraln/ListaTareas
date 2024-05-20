package com.example.listatareasfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class Fragment2: Fragment(){

    private lateinit var textViewTitle: TextView
    private lateinit var textViewDateTime: TextView
    private lateinit var textViewCompleted: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_2, container, false)
        textViewTitle = view.findViewById(R.id.textViewTitle)
        textViewDateTime = view.findViewById(R.id.textViewDateTime)
        textViewCompleted = view.findViewById(R.id.textViewCompleted)
        return view
    }

    fun updateContent(task: Task) {
        // Actualizar la UI con la información de la tarea seleccionada
        textViewTitle.text = task.title
        textViewDateTime.text = task.dateTime
        textViewCompleted.text = if (task.isCompleted==1) "Completada" else "No Completada"

    }


}