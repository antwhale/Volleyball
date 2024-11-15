package org.techtown.volleyball.base.utils

import java.text.SimpleDateFormat
import java.util.*

object Formatted {

    object Date {
        fun now(format: String = "yyyy-MM-dd HH:mm:ss"): String {
            val date = Date(System.currentTimeMillis())
            return SimpleDateFormat(format, Locale.getDefault()).format(date)
        }

        fun dateToString(date: java.util.Date?) : String {
            val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
            date?.let {
                return sdf.format(it)
            } ?: return ""
        }
    }
}