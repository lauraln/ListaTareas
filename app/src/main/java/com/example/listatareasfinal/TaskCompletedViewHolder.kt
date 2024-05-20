package com.example.listatareasfinal

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskCompletedViewHolder (view: View): RecyclerView.ViewHolder(view) {
    private val textViewTaskTitle: TextView = view.findViewById(R.id.textViewTaskTitle)
    private val textViewDateTime: TextView = view.findViewById(R.id.textViewDateTime)
    private val checkBoxCompleted: CheckBox = view.findViewById(R.id.checkBoxCompleted)

    fun render(task:Task, onItemDone:(Int) -> Unit){
        textViewTaskTitle.text =task.title
        textViewDateTime.text= task.dateTime
        if(task.isCompleted==1) {
            checkBoxCompleted.isChecked = true
        }
        itemView.setOnClickListener{ onItemDone(adapterPosition)}
    }
}