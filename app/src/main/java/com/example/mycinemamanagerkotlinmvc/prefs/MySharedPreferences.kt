package com.example.mycinemamanagerkotlinmvc.prefs

import android.content.Context

class MySharedPreferences {
    private var mContext : Context? = null

    constructor(mContext: Context?) {
        this.mContext = mContext
    }

    constructor()

    fun putStringValue(key : String?, s:String?){
        val pref  = mContext!!.getSharedPreferences(FRUITY_DROID_PREFERENCES, 0)
        val editor = pref.edit()
        editor.putString(key,s)
        editor.apply();
    }
    fun getStringValue(key: String?):String?{
        val pref = mContext!!.getSharedPreferences(FRUITY_DROID_PREFERENCES,0)
        return pref.getString(key,"")
    }


    companion object {
        private const val FRUITY_DROID_PREFERENCES = "MY_PREFERENCES"
    }
}