package ru.skillbranch.sbdelivery.utils

import android.content.Context
import android.util.TypedValue
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
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

fun String.getDate(): Date {
    return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(this)
}