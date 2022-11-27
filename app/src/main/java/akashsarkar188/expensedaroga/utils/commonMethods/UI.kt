package akashsarkar188.expensedaroga.utils.commonMethods

import akashsarkar188.expensedaroga.R
import akashsarkar188.expensedaroga.utils.currencyFormatter
import android.content.res.Resources
import android.graphics.Rect
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

fun locateView(v: View?): Rect? {
    val loc_int = IntArray(2)
    if (v == null) return null
    try {
        v.getLocationOnScreen(loc_int)
    } catch (npe: NullPointerException) {
        //Happens when the view doesn't exist on screen anymore.
        return null
    }
    val location = Rect()
    location.left = loc_int[0]
    location.top = loc_int[1]
    location.right = location.left + v.width
    location.bottom = location.top + v.height
    return location
}

fun formatAsCurrency(str: String? = null, num: Double? = null) : String {
    return try {
        val number = num ?: str?.toDouble()
        currencyFormatter.format(number)
    } catch (e : Exception) {
        e.printStackTrace()
        "0.0"
    }
}