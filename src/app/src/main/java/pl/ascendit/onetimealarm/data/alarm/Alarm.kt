package pl.ascendit.onetimealarm.data.alarm

import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.ascendit.onetimealarm.helper.TimeHelper
import java.time.LocalDateTime

@Entity(tableName = "alarm")
data class Alarm (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var datetime: LocalDateTime = LocalDateTime.now(),
    var name : String = "",
) {
    fun time24() : String {
        return TimeHelper.format(datetime)
    }

    fun hour() : Int {
        return datetime.hour
    }

    fun minute() : Int {
        return datetime.minute
    }

    fun day() : Int {
        return datetime.dayOfMonth
    }

    fun setTime(hour: Int, minute: Int) {
        datetime = datetime.withHour(hour).withMinute(minute).withSecond(0).withNano(0)
    }

    fun setMonthAndDay(month: Int, day: Int) {
        datetime = datetime.withMonth(month).withDayOfMonth(day)
    }

    fun setMonthAndDay(datetime: LocalDateTime) {
        setMonthAndDay(datetime.monthValue, datetime.dayOfMonth)
    }
}
