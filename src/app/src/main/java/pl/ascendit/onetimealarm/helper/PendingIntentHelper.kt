package pl.ascendit.onetimealarm.helper

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.Context
import android.content.Intent
import pl.ascendit.onetimealarm.StopAlarmActivity
import pl.ascendit.onetimealarm.data.alarm.Alarm
import pl.ascendit.onetimealarm.receiver.ButtonReceiver
import pl.ascendit.onetimealarm.receiver.NotificationReceiver

object PendingIntentHelper {
    fun createStopAlarm(context: Context, datetimeStr: String, alarmTime24: String, alarmName: String) : PendingIntent {
        val intent = Intent(context, StopAlarmActivity::class.java)
        intent.putExtra("ALARM_DATETIME", datetimeStr)
        intent.putExtra("ALARM_TIME24", alarmTime24)
        intent.putExtra("ALARM_NAME", alarmName)
        val reqCode = "stopalarm${datetimeStr}".hashCode()
        return PendingIntent.getActivity(context, reqCode, intent, FLAG_MUTABLE)
    }

    fun createNotificationReceiver(context: Context, alarm: Alarm) : PendingIntent {
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra("ALARM_DATETIME", alarm.datetime.toString())
        val reqCode = "nreciever${alarm.datetime.toString()}".hashCode()
        return PendingIntent.getBroadcast(context, reqCode, intent, FLAG_MUTABLE)
    }

    fun createButtonReceiver(context: Context, datetimeStr: String, notificationId: Int) : PendingIntent {
        val intent = Intent(context, ButtonReceiver::class.java)
        intent.putExtra("NOTIFICATION_ID", notificationId.toString())
        intent.putExtra("ACTION", "off")
        val reqCode = "btnreceiver${datetimeStr}".hashCode()
        return PendingIntent.getBroadcast(context, reqCode, intent, FLAG_MUTABLE)
    }
}