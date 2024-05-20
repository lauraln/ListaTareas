package com.example.listatareasfinal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val tasks:List<Task>,private val onClick: (Task) -> Unit):RecyclerView.Adapter<TaskViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater:LayoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView, onClick)
    }

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.render(tasks[position])
    }

}