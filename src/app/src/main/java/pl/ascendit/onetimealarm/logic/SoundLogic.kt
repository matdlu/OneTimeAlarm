package pl.ascendit.onetimealarm.logic

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Vibrator

object SoundLogic {
    lateinit var soundUri : Uri
    lateinit var ringtone : Ringtone
    lateinit var vibrator: Vibrator
    val audioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_ALARM)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()

    fun init(context: Context) {
        soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(context, soundUri);
        ringtone.streamType = AudioManager.STREAM_ALARM
        vibrator = context.getSystemService(VIBRATOR_SERVICE) as Vibrator
    }

    fun play() {
        if (SettingsLogic.soundOn) {
            ringtone.play();
        }
        if (SettingsLogic.vibrateOn) {
            vibrator.vibrate(longArrayOf(0, 500, 500), 0, audioAttributes)
        }
    }

    fun stop() {
        ringtone.stop();
        vibrator.cancel()
    }
}