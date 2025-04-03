package com.example.mycinemamanagerkotlinmvc.prefs

import android.content.Context
import com.example.mycinemamanagerkotlinmvc.model.User
import com.example.mycinemamanagerkotlinmvc.util.StringUtil.isEmpty
import com.google.gson.Gson

class DataStoreManager {
    private var sharePreferences: MySharedPreferences? = null

    companion object {
        private const val PREF_USER_INFOR = "PREF_USER_INFOR"
        private var instance: DataStoreManager? = null

        fun init(context: Context?) {
            instance = DataStoreManager()
            instance!!.sharePreferences = MySharedPreferences(context)

        }

        fun getInstance(): DataStoreManager? {
            return if (instance != null) {
                instance
            } else {
                throw IllegalStateException("Not initialized")
            }
        }

        fun setUser(user: User?){
            var jsonUser = ""
            if (user!=null) jsonUser = user.toJSon()
            getInstance()!!.sharePreferences!!.putStringValue(PREF_USER_INFOR, jsonUser)

        }

        fun getUser():User?{
            val jsonUser = getInstance()!!.sharePreferences!!.getStringValue(PREF_USER_INFOR)
            return if (!isEmpty(jsonUser)){
                Gson().fromJson(jsonUser,User::class.java)
            }else User()
        }
    }


}