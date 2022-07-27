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