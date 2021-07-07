package com.fachidiot.nursehro

import android.content.Context
import android.content.SharedPreferences

object MySharedPreferences {
    private val MY_ACCOUNT : String = "account"

    fun setUserId(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit() // 데이터 기록을 위한 editor
        editor.putString("MY_ID", input)
        editor.commit()
    }
    fun getUserId(context: Context) : String {
        val prefs : SharedPreferences = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        return prefs.getString("MY_ID", "").toString()
    }
    fun setUserPW(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit() // 데이터 기록을 위한 editor
        editor.putString("MY_PW", input)
        editor.commit()
    }
    fun getUserPW(context: Context) : String {
        val prefs : SharedPreferences = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        return prefs.getString("MY_PW", "").toString()
    }
    fun setAuto(context: Context, input: Boolean) {
        val prefs : SharedPreferences = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit() // 데이터 기록을 위한 editor
        editor.putBoolean("AUTO", input)
        editor.commit()
    }
    fun getAuto(context: Context) : Boolean {
        val prefs : SharedPreferences = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("AUTO", false)
    }
}