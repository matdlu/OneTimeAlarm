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

package pl.ascendit.onetimealarm

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import pl.ascendit.onetimealarm.databinding.ActivityStopAlarmBinding
import pl.ascendit.onetimealarm.logic.AlarmLogic


class StopAlarmActivity : AppCompatActivity() {
    val lTag = "StopAlarmActivity"
    lateinit var binding: ActivityStopAlarmBinding
    var notificationId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(lTag, "onCreate")

        getSupportActionBar()?.hide()

        val datetimeStr = intent.getStringExtra("ALARM_DATETIME")
        if ( datetimeStr == null ) {
            Log.e(lTag, "ALARM_DATETIME is null")
            finish()
            return
        }
        notificationId = AlarmLogic.getNotificationId(datetimeStr)

        val alarmTime24 = intent.getStringExtra("ALARM_TIME24")
        if ( alarmTime24 == null ) {
            Log.e(lTag, "ALARM_TIME24 is null")
        }

        val alarmName = intent.getStringExtra("ALARM_NAME")
        if ( alarmName == null ) {
            Log.e(lTag, "ALARM_NAME is null")
        }

        binding = ActivityStopAlarmBinding.inflate(layoutInflater)
        binding.tvTime.text = alarmTime24
        binding.tvName.text = alarmName
        binding.btAlarmOff.setOnClickListener {
            AlarmLogic.alarmOff(baseContext, notificationId!!)
            finish()
        }

        setContentView(binding.root)
    }
}