package pl.ascendit.onetimealarm.logic

import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager

object SettingsLogic {
    val lTag = "SettingsLogic"
    val ringtoneDefault = ""
    val soundDefault = true
    val vibrateDefault = true

    var vibrateOn: Boolean = vibrateDefault
    var soundOn: Boolean = soundDefault

    fun load(context: Context) {
        Log.d(lTag, "preferences loading")
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        soundOn = preferences.getBoolean("sound", soundDefault)
        vibrateOn = preferences.getBoolean("vibrate", vibrateDefault)
        Log.d(lTag, "preferences loaded")
    }

    fun setSound(value: Boolean) {
        soundOn = value
    }

    fun setVibrate(value: Boolean) {
        vibrateOn = value
    }

}