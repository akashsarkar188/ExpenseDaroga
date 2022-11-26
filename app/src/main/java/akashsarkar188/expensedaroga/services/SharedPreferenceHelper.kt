package akashsarkar188.expensedaroga.services

import akashsarkar188.expensedaroga.utils.MyApp
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

    private val SHOULD_BUBBLE = "shouldBubble"
}