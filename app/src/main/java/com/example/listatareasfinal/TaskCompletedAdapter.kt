package com.example.listatareasfinal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TaskCompletedAdapter(private val tasksCompleted:List<Task>, private val onItemdone: (Int) -> Unit):
    RecyclerView.Adapter<TaskCompletedViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskCompletedViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        return TaskCompletedViewHolder(layoutInflater.inflate(R.layout.item_task, parent,false))
    }

    override fun getItemCount() = tasksCompleted.size

    override fun onBindViewHolder(holder: TaskCompletedViewHolder, position: Int) {
        holder.render(tasksCompleted[position], onItemdone)
    }

}