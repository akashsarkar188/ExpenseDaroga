package akashsarkar188.expensedaroga.utils.commonMethods

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateTimeFormatHelper {
    val MONTH_FORMAT = "MMM"
    val FULL_MONTH_FORMAT = "MMMM"
    val YEAR_FORMAT = "yyyy"
    val DATE_FORMAT = "dd"
    val HOUR_FORMAT_12 = "hh"
    val MINUTE_FORMAT = "mm"
    val AM_PM_FORMAT = "a"
    val MONTH_YEAR_FORMAT = "$MONTH_FORMAT $YEAR_FORMAT"
    val FULL_MONTH_YEAR_FORMAT = "$FULL_MONTH_FORMAT $YEAR_FORMAT"
    val DATE_FORMAT_01_JAN = "$DATE_FORMAT $MONTH_FORMAT"
    val TIME_FORMAT_HH_MM_A = "$HOUR_FORMAT_12:$MINUTE_FORMAT $AM_PM_FORMAT"
}

fun getCurrentMonthYearString(): String {
    return SimpleDateFormat(DateTimeFormatHelper.MONTH_YEAR_FORMAT, Locale.US).format(Date())
}

fun getCurrentFullMonthYearString(): String {
    return SimpleDateFormat(DateTimeFormatHelper.FULL_MONTH_YEAR_FORMAT, Locale.US).format(Date())
}

fun getFullMonthYearStringFromMonthYear(monthYear: String): String {
    val date = SimpleDateFormat(DateTimeFormatHelper.MONTH_YEAR_FORMAT, Locale.US).parse(monthYear)
    return SimpleDateFormat(DateTimeFormatHelper.FULL_MONTH_YEAR_FORMAT, Locale.US).format(date)
}

fun getMonthYearStringFromDate(date: Date): String {
    return SimpleDateFormat(DateTimeFormatHelper.MONTH_YEAR_FORMAT, Locale.US).format(date)
}

fun getCurrentDateObject(): Date {
    return Date()
}

fun getLastDateForMonthYear(monthYear: String): Date {
    val calendar = Calendar.getInstance()

    calendar.time = getFirstDateForMonthYear(monthYear)

    calendar[Calendar.DAY_OF_MONTH] = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    calendar[Calendar.HOUR_OF_DAY] = 11
    calendar[Calendar.MINUTE] = 59

    return calendar.time
}

fun getFirstDateForMonthYear(monthYearStr: String): Date {
    val mMonth: Int = getMonthIntFromMonthYearStr(monthYearStr)
    val mYear: Int = getYearIntFromMonthYearStr(monthYearStr)
    val c = Calendar.getInstance()
    c[Calendar.MONTH] = mMonth
    c[Calendar.YEAR] = mYear
    c[Calendar.DAY_OF_MONTH] = 1
    c[Calendar.HOUR_OF_DAY] = 0
    return c.time
}

fun getMonthIntFromMonthYearStr(monthYearStr: String?): Int {
    return try {
        val date = SimpleDateFormat(DateTimeFormatHelper.MONTH_YEAR_FORMAT, Locale.US).parse(monthYearStr)
        val cal = Calendar.getInstance()
        cal.time = date
        cal[Calendar.MONTH]
    } catch (e: Exception) {
        e.printStackTrace()
        0
    }
}

fun getYearIntFromMonthYearStr(monthYearStr: String?): Int {
    return if (monthYearStr == null) {
        getCurrentYear()
    } else {
        var date: Date? = null
        try {
            date = SimpleDateFormat(DateTimeFormatHelper.MONTH_YEAR_FORMAT, Locale.US)
                .parse(monthYearStr)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val cal = Calendar.getInstance()
        cal.time = date
        cal[Calendar.YEAR]
    }
}

fun getCurrentYear(): Int {
    val c = Calendar.getInstance()
    return c[Calendar.YEAR]
}

fun getDateInDD_MMM(date : Date) : String {
    return SimpleDateFormat(DateTimeFormatHelper.DATE_FORMAT_01_JAN, Locale.US).format(date)
}

fun getTimeInHH_MM_A(date : Date) : String {
    return SimpleDateFormat(DateTimeFormatHelper.TIME_FORMAT_HH_MM_A, Locale.US).format(date)
}