package ru.skillbranch.sbdelivery.utils

import android.content.Context
import android.util.TypedValue
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun Int.toDp(context: Context):Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
).toInt()

fun Float.removeZero(): String{
    val format = DecimalFormat()
    format.isDecimalSeparatorAlwaysShown = false
    return format.format(this)
}

fun String.parseDate(): String {
    val tradeDate = getDate()
    return SimpleDateFormat(
        "dd.MM.yyyy",
        Locale.ENGLISH
    ).format(tradeDate)
}

fun String.getDate(): Date {
    return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(this)
}