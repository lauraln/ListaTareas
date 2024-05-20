package com.example.listatareasfinal

import android.app.Application

//Instanciar cosas del proyecto
class TaskApplication:Application() {
    companion object{
       lateinit var prefs: Preferences
    }
    override fun onCreate() {
        super.onCreate()
        prefs=Preferences(baseContext)
    }
}