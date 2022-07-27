package pl.ascendit.onetimealarm.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import pl.ascendit.onetimealarm.R
import pl.ascendit.onetimealarm.data.alarm.Alarm
import pl.ascendit.onetimealarm.helper.PendingIntentHelper
import pl.ascendit.onetimealarm.logic.AlarmLogic
import pl.ascendit.onetimealarm.logic.SoundLogic
import java.time.LocalDateTime

class NotificationReceiver : BroadcastReceiver() {
    private val lTag = "NotificationReciever"

    override fun onReceive(context: Context, intent: Intent) {
        val offAlarmActionTitle = context.getString(R.string.stop_alarm_turn_off_alarm)

        val datetimeStr = intent.getStringExtra("ALARM_DATETIME")
        if (datetimeStr == null) {
            Log.e(lTag,"datetimeStr is null")
            return
        }

        val datetime = LocalDateTime.parse(datetimeStr)
        Log.d(lTag, "datetime: ${datetimeStr}")

        val triggered: Alarm? = AlarmLogic.getTriggeredAlarm(datetime)
        if (triggered == null) {
            Log.e(lTag, "Alarm triggered but no alarm found in database.")
            return
        }
        Log.d(lTag, "triggered alarm: ${triggered}")
        AlarmLogic.deleteAlarm(context, datetimeStr, true)

        val notificationId = AlarmLogic.getNotificationId(datetimeStr)

        val buttonReceiverPendingIntent = PendingIntentHelper.createButtonReceiver(context, datetimeStr, notificationId)
        val stopAlarmPendingIntent = PendingIntentHelper.createStopAlarm(context, datetimeStr, triggered.time24(), triggered.name)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_baseline_alarm_24)
            setContentTitle(triggered.time24())
            if ( ! triggered.name.isEmpty() ) {
                setContentText(triggered.name)
            }
            setPriority(NotificationCompat.PRIORITY_HIGH)
            setCategory(NotificationCompat.CATEGORY_ALARM)
            setFullScreenIntent(stopAlarmPendingIntent, true)
            addAction(R.drawable.ic_baseline_alarm_off_24, offAlarmActionTitle, buttonReceiverPendingIntent)
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        with(notificationManager) {
            val notification = builder.build()
            notify(notificationId, notification)
        }

        SoundLogic.play()
    }

    companion object {
        private val CHANNEL_ID = "otaalarmchannel"

        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = context.getString(R.string.notification_channel_name)
                val descriptionText = context.getText(R.string.notification_channel_desc)
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText as String?
                    setSound(null, null)
                }
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}
