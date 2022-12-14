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

package pl.ascendit.onetimealarm.fragment

import android.app.Activity
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.*
import pl.ascendit.onetimealarm.logic.SettingsLogic
import pl.ascendit.onetimealarm.R


class SettingsFragment : PreferenceFragmentCompat() {
    val lTag = "SettingsFragment"
    lateinit var ringtonePickerResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val context = preferenceManager.context
        val screen = preferenceManager.createPreferenceScreen(context)

        val ringtoneTitle = getString(R.string.choose_ringtone)
        val soundTitle = getString(R.string.alarm_sound)
        val vibrateTitle = getString(R.string.vibrate)
        val clock12Title = getString(R.string.clock12)

        val ringtonePreference = Preference(context)
        ringtonePreference.setDefaultValue(SettingsLogic.ringtoneDefault)
        ringtonePreference.setIcon(R.drawable.ic_baseline_alarm_24)
        ringtonePreference.apply {
            key = "ringtone"
            title = ringtoneTitle
        }
        ringtonePreference.onPreferenceClickListener = RingtonePreferenceClickListener()

        val soundPreference = SwitchPreferenceCompat(context)
        soundPreference.setDefaultValue(SettingsLogic.soundDefault)
        soundPreference.setIcon(R.drawable.ic_baseline_volume_up_24)
        soundPreference.apply {
            key = "sound"
            title = soundTitle
        }
        soundPreference.onPreferenceChangeListener = object : Preference.OnPreferenceChangeListener {
            override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
                val value = newValue as Boolean
                Log.d(lTag, "sound set to ${value}")
                SettingsLogic.setSound(value)
                return true
            }
        }

        val vibratePreference = SwitchPreferenceCompat(context)
        vibratePreference.setDefaultValue(SettingsLogic.vibrateDefault)
        vibratePreference.setIcon(R.drawable.ic_baseline_vibration_24)
        vibratePreference.apply {
            key = "vibrate"
            title = vibrateTitle
        }
        vibratePreference.onPreferenceChangeListener = object : Preference.OnPreferenceChangeListener {
            override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
                val value = newValue as Boolean
                Log.d(lTag, "vibrate set to ${value}")
                SettingsLogic.setVibrate(value)
                return true
            }
        }

        val clock12Preference = SwitchPreferenceCompat(context)
        clock12Preference.setDefaultValue(SettingsLogic.clock12Default)
        clock12Preference.setIcon(R.drawable.ic_baseline_access_time_24)
        clock12Preference.apply {
            key = "clock12"
            title = clock12Title
        }
        clock12Preference.onPreferenceChangeListener = object : Preference.OnPreferenceChangeListener {
            override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
                val value = newValue as Boolean
                Log.d(lTag, "clock12 set to ${value}")
                SettingsLogic.setClock12(value)
                return true
            }
        }

        screen.addPreference(ringtonePreference)
        screen.addPreference(soundPreference)
        screen.addPreference(vibratePreference)
        screen.addPreference(clock12Preference)
        preferenceScreen = screen
    }

    fun register(activity: AppCompatActivity) {
        ringtonePickerResultLauncher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri: Uri? = result.data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
                Log.d(lTag, "picked ringtone uri: ${uri}")
                if (uri != null) {
                    RingtoneManager.setActualDefaultRingtoneUri(
                        activity.baseContext,
                        RingtoneManager.TYPE_ALARM,
                        uri
                    )
                }
            }
        }
    }

    fun createRingtonePickerIntent() : Intent {
        val currentTone: Uri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM)
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentTone)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
        return intent
    }

    inner class RingtonePreferenceClickListener() : Preference.OnPreferenceClickListener {
        override fun onPreferenceClick(preference: Preference): Boolean {
            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) && (! Settings.System.canWrite(context))) {
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                startActivity(intent)
            } else {
                val ringtonePickerIntent = createRingtonePickerIntent()
                ringtonePickerResultLauncher.launch(ringtonePickerIntent)
            }
            return true
        }
    }
}
