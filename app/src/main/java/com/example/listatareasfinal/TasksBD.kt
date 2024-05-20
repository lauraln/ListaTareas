package com.example.listatareasfinal

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class TasksBD (val contexto: Context) :
    SQLiteOpenHelper(contexto, "task", null, 1) {
    override fun onCreate(bd: SQLiteDatabase) {

        bd.execSQL("""
                CREATE TABLE tasks (
                    _id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT,
                    date TEXT,
                    completed INTEGER
                )
           """)

        bd.beginTransaction()
        try {
            bd.execSQL("INSERT INTO tasks (title, date, completed) VALUES ('Sacar al perro 55', '20-01-2024 10:20', 0)")
            bd.execSQL("INSERT INTO tasks (title, date, completed) VALUES ('Bajar la basura', '20-01-2024 10:20', 1)")
            bd.execSQL("INSERT INTO tasks (title, date, completed) VALUES ('Tocar el piano', '20-01-2024 10:20', 0)")
            bd.setTransactionSuccessful()
        } finally {
            bd.endTransaction()
        }
    }

   override  fun onUpgrade(db: SQLiteDatabase, oldVersion: Int,
                           newVersion: Int) {}



    fun getInCompletedTasks(): Cursor {
        val db = this.readableDatabase
        val cursor=db.rawQuery("SELECT * FROM tasks WHERE completed=0", null)
        Log.d("DatabaseOperation", "Number of incomplete tasks: ${cursor.count}")
        return cursor
    }

    fun updateTask(task:Task): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("title", task.title)
            put("date", task.dateTime)
            put("completed", task.isCompleted)
        }
        val success = db.update("tasks", values, "_id = ?", arrayOf(task.id.toString()))
        db.close()
        return success
    }

    fun deleteTask(id: Long): Int {
        val db = this.writableDatabase
        val success = db.delete("tasks", "_id = ?", arrayOf(id.toString()))
        db.close()
        return success
    }



    fun resetDatabase() {
        val db = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS tasks")
        // Repite para todas las tablas en tu base de datos
        onCreate(db)
    }

    fun deleteDatabase(context: Context): Boolean {
        return context.deleteDatabase("tasks")
    }


    fun addTask(task:Task): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("title", task.title)
            put("date", task.dateTime)
            put("completed", task.isCompleted)
        }
        val id =db.insert("tasks", null, values)
        db.close()
        return id
    }

    fun getAllTasks(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM tasks", null)
    }

    fun getCompletedTasks(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM tasks WHERE completed=1", null)
    }

}