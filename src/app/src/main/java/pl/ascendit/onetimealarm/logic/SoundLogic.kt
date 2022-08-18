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