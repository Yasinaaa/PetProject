package ru.skillbranch.sbdelivery.utils

import android.content.Context
import android.util.TypedValue
import java.lang.String.format
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

fun Int.toDp(context: Context): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
).toInt()

fun Float.removeZeroAndReplaceComma(): String{
    val format = DecimalFormat()
    format.isDecimalSeparatorAlwaysShown = false
    val priceWithComma = format.format(this)
    return if(priceWithComma.contains(",")){
        priceWithComma.replace(",", " ")
    }else
        priceWithComma
}

fun String.parseDate(): String {
    val tradeDate = getDate()
    return SimpleDateFormat(
        "dd.MM.yyyy",
        Locale.ENGLISH
    ).format(tradeDate)
}

fun String?.parseMilliseconds(): String{
    return if(this != null) {
        if(isNotBlank()) {
            val cal = Calendar.getInstance()
            cal.timeInMillis = this.toLong()
            val formatter = SimpleDateFormat(
                "dd MMMM yyyy",
                Locale("ru")
            )
            val formattedString: String = formatter.format(cal.time)
            formattedString
        } else ""
    }else ""
}

fun String.getDate(): Date {
    return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(this)
}