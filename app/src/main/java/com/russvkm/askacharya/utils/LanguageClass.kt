package com.russvkm.askacharya.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.russvkm.askacharya.R
import com.russvkm.askacharya.activity.FlashActivity
import java.util.*

open class LanguageClass(private val context: Context) {
    private var languageCode:Int=0
    fun english(){
        languageCode=1
        val config=context.resources.configuration
        val locale= Locale("en")
        Locale.setDefault(locale)
        config.locale = locale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        savingPreferences(languageCode)
    }
    fun hindi() {
        languageCode = 2
        val config = context.resources.configuration
        val locale = Locale("hi")
        Locale.setDefault(locale)
        config.locale = locale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        savingPreferences(languageCode)
    }


    private fun savingPreferences(languageCode:Int){
        val sharedPreferences=context.getSharedPreferences(context.getString(R.string.languagePref),Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor=sharedPreferences.edit()
        editor.putInt("Language",languageCode)
        editor.apply()
    }

    fun fetchingPreferences():Int{
        val sharedPreferences=context.getSharedPreferences(context.getString(R.string.languagePref),Context.MODE_PRIVATE)
        return sharedPreferences.getInt("Language",0)
    }

    fun creatingDialog(activity: Activity){
        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.restart_app))
            .setMessage(context.getString(R.string.restart_app_message))
            .setPositiveButton(context.getString(R.string.restart)){
                    _, _ ->
                restartingApp()
                activity.finishAffinity()
            }
            .setNegativeButton(context.getString(R.string.cancel)){
                    dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun restartingApp(){
        val intent= Intent(context,FlashActivity::class.java)
        context.startActivity(intent)
    }
}