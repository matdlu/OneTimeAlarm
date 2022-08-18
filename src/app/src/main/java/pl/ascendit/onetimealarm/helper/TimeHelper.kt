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