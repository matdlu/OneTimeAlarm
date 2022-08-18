/*
 * Copyright (c) 2022 Ascendit Sp. z o. o.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/
 *
 */

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
