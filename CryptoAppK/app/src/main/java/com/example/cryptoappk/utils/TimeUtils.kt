package com.example.cryptoappk.utils

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun convertTimestampToTime(timestamp: Long?): String {
    if (timestamp == null) return ""
    val stamp = Timestamp(timestamp * 1000)//Timestamp принимает милисекунды, а с сервера приходят секунды
    val date = Date(stamp.time)
    val pattern = "HH:mm:ss"//hh с маленькой буквы 12 часовой формат
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(date)
}