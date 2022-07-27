package pl.ascendit.onetimealarm.data.converter

import androidx.room.TypeConverter
import java.time.LocalDateTime

class LocalDateTimeConverter {
    @TypeConverter
    fun fromStringToLocalDateTime(str: String?): LocalDateTime? {
        return if (str == null) {
            null
        } else {
            LocalDateTime.parse(str)
        }
    }

    @TypeConverter
    fun fromLocalDateTimeToString(datetime: LocalDateTime?): String? {
        return if (datetime == null) {
            null
        } else {
            datetime.toString()
        }
    }
}