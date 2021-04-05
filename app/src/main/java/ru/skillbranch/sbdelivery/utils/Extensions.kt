package ru.skillbranch.sbdelivery.utils

import android.content.Context
import android.util.TypedValue
import java.text.DecimalFormat

fun Int.toDp(context: Context):Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
).toInt()

fun Float.removeZero(): String{
    val format = DecimalFormat()
    format.isDecimalSeparatorAlwaysShown = false
    return format.format(this)
}