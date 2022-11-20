package akashsarkar188.expensedaroga.utils.commonMethods

import akashsarkar188.expensedaroga.R
import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.animation.AnimationUtils
import com.google.android.material.textfield.TextInputEditText


fun TextInputEditText.isNullOrEmpty(): Boolean {
    text?.toString()?.let {
        return it.isEmpty()
    }
    return true
}

fun TextInputEditText.toDouble(): Double {
    text?.toString()?.let {
        try {
            return it.toDouble()
        } catch (e: Exception) {
            // if we get numberFormatException it will return 0.0
        }

    }
    return 0.0
}

fun String.capitalize() : String {
    if (this.isNotEmpty()) {
        return substring(0, 1).uppercase() + substring(1).lowercase()
    }
    return this
}

fun View.shakeView() {
    val shake = AnimationUtils.loadAnimation(this.context, R.anim.shake)
    startAnimation(shake)
}

val Number.toPx get() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics)