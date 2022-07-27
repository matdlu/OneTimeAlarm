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
