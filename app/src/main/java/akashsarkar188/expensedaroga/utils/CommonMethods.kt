package akashsarkar188.expensedaroga.utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeFormatHelper {
    val MONTH_FORMAT = "MMM"
    val YEAR_FORMAT = "yyyy"
    val MONTH_YEAR_FORMAT = "$MONTH_FORMAT $YEAR_FORMAT"
}

fun getCurrentMonthYearString(): String {
    return SimpleDateFormat(DateTimeFormatHelper.MONTH_YEAR_FORMAT, Locale.US).format(Date())
}