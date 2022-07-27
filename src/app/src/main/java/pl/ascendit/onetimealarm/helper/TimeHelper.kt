package pl.ascendit.onetimealarm.helper

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object TimeHelper {
    private val sep = ":"
    val formatter =  DateTimeFormatter.ofPattern("HH${sep}mm")

    fun format(datetime: LocalDateTime) : String {
        return datetime.format(formatter)
    }

    fun toEpochMilli(datetime: LocalDateTime) : Long {
        val instant = datetime.atZone(ZoneId.systemDefault()).toInstant()
        return instant.toEpochMilli()
    }

    fun isToday(datetime: LocalDateTime) : Boolean {
        return datetime.toLocalDate().isEqual(LocalDate.now())
    }

    fun isTommorow(datetime: LocalDateTime) : Boolean {
        return datetime.toLocalDate().isEqual(LocalDate.now().plusDays(1))
    }

    fun isNow(datetime: LocalDateTime, minuteError: Int = 1) : Boolean {
        val now = LocalDateTime.now()
        return isToday(datetime) &&
                datetime.hour == now.hour &&
                (datetime.minute == now.minute || datetime.minute-minuteError == now.minute || datetime.minute+minuteError == now.minute)
    }
}