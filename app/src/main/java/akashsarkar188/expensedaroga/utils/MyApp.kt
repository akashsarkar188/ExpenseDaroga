package akashsarkar188.expensedaroga.utils

import android.app.Application
import android.content.Context

class MyApp : Application() {

    init {
        instance = this
    }

    companion object {
        private lateinit var instance: MyApp

        fun getInstance(): MyApp {
            return instance
        }

        fun getApplicationContext() : Context {
            return instance
        }
    }
}