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
            Log.e(lTag, "notificationId is null")
            return
        }

        val action = intent.getStringExtra("ACTION")
        if ( action == "off") {
            Log.d(lTag, "alarm turned off ")
            AlarmLogic.alarmOff(context, Integer.parseInt(notificationId))
        } else if ( action == null ) {
            Log.e(lTag, "action is null")
            return
        } else {
            Log.e(lTag, "action has unknown value: ${action}")
            return
        }
    }

}