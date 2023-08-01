package com.gramzin.noteapp.data.data_storage

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class FirstTimeLaunchAppStorage @Inject constructor(
    @ApplicationContext
    private val context: Context
) {

    fun checkIsFirstLaunch(): Boolean{
        val prefs = context.getSharedPreferences("isFirstLaunch", Context.MODE_PRIVATE)
        val isFirstLaunch = prefs.getBoolean("isFirstLaunch", true)
        if (isFirstLaunch) {
            val editor = prefs.edit()
            editor.putBoolean("isFirstLaunch", false)
            editor.apply()

        }
        return isFirstLaunch
    }


}