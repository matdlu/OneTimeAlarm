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