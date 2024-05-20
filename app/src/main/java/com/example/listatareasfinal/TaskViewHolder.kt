package com.example.listatareasfinal

import android.view.ContextMenu
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskViewHolder (view: View, private val onClick: (Task) -> Unit):RecyclerView.ViewHolder(view), View.OnCreateContextMenuListener {
    private val textViewTaskTitle: TextView = view.findViewById(R.id.textViewTaskTitle)
    private val textViewDateTime: TextView = view.findViewById(R.id.textViewDateTime)
    private val checkBoxCompleted: CheckBox = view.findViewById(R.id.checkBoxCompleted)

    init {
        itemView.setOnCreateContextMenuListener(this)

        itemView.setOnLongClickListener {
            itemView.showContextMenu()
            true // Indica que el evento ha sido consumido
        }
    }

    fun render(task:Task){
        textViewTaskTitle.text =task.title
        textViewDateTime.text= task.dateTime
        if(task.isCompleted==1) {
            checkBoxCompleted.isChecked = true
        }
        itemView.setOnClickListener { onClick(task) }

    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menu.add(adapterPosition, R.id.ToDoTask, 0, "Completar Tarea")
        menu.add(adapterPosition, R.id.Deletetask, 1, "Eliminar Tarea")
        menu.add(adapterPosition, R.id.Updatetask, 2, "Modificar Tarea")
        menu.add(adapterPosition, R.id.Sendtask, 3, "Enviar Tarea")
    }
}