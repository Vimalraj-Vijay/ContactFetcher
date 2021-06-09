package com.vimalvijay.contactfetcher.helpers

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor


class SharedDatas(val context: Context) {
    val editor: Editor = context.getSharedPreferences("APP_contacts", MODE_PRIVATE).edit()

    fun saveData(key: String, value: String) {
        editor.clear()
        editor.putString(key, value)
        editor.apply()
    }

    fun getData(key: String): String? {
        val prefs: SharedPreferences = context.getSharedPreferences("APP_contacts", MODE_PRIVATE)
        return prefs.getString(key, "")
    }
}