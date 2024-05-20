package com.example.listatareasfinal

data class Task(
    var id: Long? = null,  // Hacer el ID opcional y con valor predeterminado null
    var title: String,
    var dateTime: String,  // Almacena la fecha y hora como un String para simplificar
    var isCompleted: Int
)