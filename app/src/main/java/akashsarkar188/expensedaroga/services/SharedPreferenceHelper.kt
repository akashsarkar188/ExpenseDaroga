package akashsarkar188.expensedaroga.services

import akashsarkar188.expensedaroga.utils.MyApp
import akashsarkar188.expensedaroga.utils.ObjectFactory
import android.content.Context.MODE_PRIVATE

import android.content.SharedPreferences

object SharedPreferenceHelper {
    private var sharedPreferences: SharedPreferences? = null

    init {
        sharedPreferences =
            MyApp.getApplicationContext().getSharedPreferences("daroga", MODE_PRIVATE)
    }

    fun shouldBubble(): Boolean {
        return sharedPreferences?.getBoolean(SHOULD_BUBBLE, false) ?: false
    }

    fun setShouldBubble(shouldBubble: Boolean) {
        sharedPreferences?.apply {
            val editor = edit()
            editor.putBoolean(SHOULD_BUBBLE, shouldBubble)
            editor.apply()
        }
    }

    fun readSms(): Boolean {
        return sharedPreferences?.getBoolean(READ_SMS, false) ?: false
    }

    fun setReadSms(readSms: Boolean) {
        sharedPreferences?.apply {
            val editor = edit()
            editor.putBoolean(READ_SMS, readSms)
            editor.apply()
        }
    }

    fun getUserName(): String {
        return sharedPreferences?.getString(USER_NAME, "Akash") ?: "Akash"
    }

    fun setUserName(userName: String) {
        sharedPreferences?.apply {
            val editor = edit()
            editor.putString(USER_NAME, userName)
            editor.apply()
        }
        ObjectFactory.userNameChangeMutableLiveData.value = true
    }

    private val SHOULD_BUBBLE = "shouldBubble"
    private val USER_NAME = "userName"
    private val READ_SMS = "readSms"
}