package akashsarkar188.expensedaroga.utils.commonMethods

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


fun closeKeyboard(context: Context, view: View? = null) {
    try {
        view?.let {
            val imm: InputMethodManager =
                context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        if (context is Activity) {
            //Find the currently focused view, so we can grab the correct window token from it.
            var focusedView: View? = context.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (focusedView == null) {
                focusedView = View(context)
            }
            (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(focusedView.windowToken, 0)
        }
    } catch (e: Exception) {

    }
}

inline fun doIfTrue(b: Boolean?, block: () -> Unit) {
    if (b == true) block()
}

inline fun doIfFalse(b: Boolean?, block: () -> Unit) {
    if (b == false) block()
}

inline fun doIfFalseOrNull(b: Boolean?, block: () -> Unit) {
    if (b != true) block()
}