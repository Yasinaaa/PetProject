package ru.skillbranch.sbdelivery.utils

import android.content.Context
import android.util.TypedValue

fun Int.toDp(context: Context):Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
).toInt()
