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

package pl.ascendit.onetimealarm.receiver;

import android.app.AlarmManager
import android.content.BroadcastReceiver;
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import pl.ascendit.onetimealarm.data.alarm.AlarmDatabase
import pl.ascendit.onetimealarm.logic.AlarmLogic

/* Restarts alarms after reboot or update */
class UpdateOrRebootReceiver : BroadcastReceiver() {
    val lTag = "UpdateOrRebootReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(lTag, "received")
        AlarmLogic.alarmDatabase = AlarmDatabase.open(context)
        AlarmLogic.alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        AlarmLogic.restartAllAlarms(context)
    }
}
