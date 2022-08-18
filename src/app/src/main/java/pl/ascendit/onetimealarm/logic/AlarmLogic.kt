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

package pl.ascendit.onetimealarm.logic

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import pl.ascendit.onetimealarm.able.Loadable
import pl.ascendit.onetimealarm.able.Updatable
import pl.ascendit.onetimealarm.data.alarm.Alarm
import pl.ascendit.onetimealarm.data.alarm.AlarmDatabase
import pl.ascendit.onetimealarm.helper.PendingIntentHelper
import pl.ascendit.onetimealarm.helper.TimeHelper
import java.time.*
import kotlin.concurrent.thread


object AlarmLogic {
    val lTag = "AlarmLogic"
    lateinit var alarmDatabase: AlarmDatabase
    lateinit var alarmManager: AlarmManager
    lateinit var loadable: Loadable
    var updatable: Updatable? = null
    var initialized = false

    private fun scheduleAlarm(context: Context, alarm: Alarm) {
        Log.d(lTag, "scheduling alarm: ${alarm}")
        val pendingIntent = PendingIntentHelper.createNotificationReceiver(context, alarm)
        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(TimeHelper.toEpochMilli(alarm.datetime), pendingIntent),
            pendingIntent
        )
    }

    private fun unscheduleAlarm(context: Context, alarm: Alarm) {
        Log.d(lTag, "unscheduling alarm: ${alarm}")
        val pendingIntent = PendingIntentHelper.createNotificationReceiver(context, alarm)
        alarmManager.cancel(pendingIntent)
    }

    fun addAlarm(context: Context, alarm: Alarm) {
        updatable?.add(alarm)
        thread {
            alarmDatabase.alarms.add(alarm)
        }
        scheduleAlarm(context, alarm)
    }

    fun updateAlarm(context: Context, newAlarm: Alarm) {
        updatable?.replace(newAlarm)
        thread {
            val oldAlarm = alarmDatabase.alarms.get(newAlarm.id)
            alarmDatabase.alarms.update(newAlarm)
            unscheduleAlarm(context, oldAlarm)
        }
        scheduleAlarm(context, newAlarm)
    }

    fun deleteAlarm(context: Context, alarm: Alarm, fromReceiver: Boolean = false) {
        updatable?.remove(alarm)
        thread {
            alarmDatabase.alarms.delete(alarm)
            if (fromReceiver) {
                loadable.loadData()
            }
        }
        unscheduleAlarm(context, alarm)
    }

    fun deleteAlarm(context: Context, datetimeStr: String, fromReceiver: Boolean = false) {
        val triggered = getTriggeredAlarm(LocalDateTime.parse(datetimeStr))
        if ( triggered == null ) {
            Log.e(lTag, "Alarm triggered but no alarm found in database.")
            return
        }
        Log.d(lTag, "triggered alarm: ${triggered}")
        deleteAlarm(context, triggered, fromReceiver)
    }

    fun getTriggeredAlarm(alarmDatetime: LocalDateTime) : Alarm? {
        var triggered: Alarm? = null
        thread {
            for (alarm in alarmDatabase.alarms.getAll()) {
                if (alarm.datetime.isEqual(alarmDatetime)) {
                    triggered = alarm
                    Log.d(lTag, "Alarm found: ${triggered}")
                    break
                }
            }
        }.join()
        return triggered
    }

    fun getNotificationId(datetimeStr: String) : Int {
        return "notification${datetimeStr}".hashCode()
    }

    fun alarmOff(context: Context, notificationId: Int) {
        SoundLogic.stop()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }


    fun restartAllAlarms(context: Context) {
        Log.d(lTag, "restarting all alarms")
        thread {
            for (alarm in alarmDatabase.alarms.getAll()) {
                scheduleAlarm(context, alarm)
            }
        }
    }
}