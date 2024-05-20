package com.example.listatareasfinal

import android.content.Context
import android.content.SharedPreferences

class Preferences (context: Context) {
    //Las constantes tienen que estar aquí
    companion object{
        const val PREFS_NAME = "myDatabase"
    }


    val prefs:SharedPreferences = context.getSharedPreferences(PREFS_NAME,0)

    fun saveTasks(tasks:List<String>){
        //prefs.edit().putString("value","ejemplo").apply()
        //Guardamos la info
        prefs.edit().putStringSet("value",tasks.toSet()).apply()
    }

    fun getTasks():MutableList<String>{
        //return prefs.getStringSet("value", emptySet<String>())!!.toMutableList()
        return prefs.getStringSet("value", emptySet<String>())?.toMutableList() ?: mutableListOf()
    }
}