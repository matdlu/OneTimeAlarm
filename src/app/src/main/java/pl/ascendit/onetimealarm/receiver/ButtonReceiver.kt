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

package pl.ascendit.onetimealarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import pl.ascendit.onetimealarm.logic.AlarmLogic

/* Receives when Button is pressed on notification */
class ButtonReceiver : BroadcastReceiver() {
    val lTag = "ButtonReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getStringExtra("NOTIFICATION_ID")
        if ( notificationId == null ) {
            return
        }

        val action = intent.getStringExtra("ACTION")
        if ( action == "off") {
            AlarmLogic.alarmOff(context, Integer.parseInt(notificationId))
        } else if ( action == null ) {
            return
        } else {
            return
        }
    }

}