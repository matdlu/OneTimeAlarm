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

package pl.ascendit.onetimealarm.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import pl.ascendit.onetimealarm.R
import pl.ascendit.onetimealarm.data.alarm.Alarm
import pl.ascendit.onetimealarm.databinding.DialogAlarmEditBinding
import pl.ascendit.onetimealarm.helper.TimeHelper
import pl.ascendit.onetimealarm.helper.TimePickerHelper
import java.time.LocalDateTime

class DialogAlarmEdit(var alarm: Alarm) : DialogFragment() {
    val lTag = "DialogAlarmEdit"
    private lateinit var binding : DialogAlarmEditBinding
    private lateinit var timePicker: MaterialTimePicker
    var onAlarmSaved: (alarm: Alarm) -> Unit = {}
    var onAlarmCanceled: (alarm: Alarm) -> Unit = {}
    var onAlarmDeleted: (alarm: Alarm) -> Unit = {}
    var addMode = false

    fun setTime(hour: Int, minute: Int) {
        alarm.setTime(hour, minute)
        binding.tfTime.editText?.setText(TimeHelper.format(alarm.datetime))
    }

    private fun getItemPosition() : Int {
        if ( TimeHelper.isTommorow(alarm.datetime) ) return 1 else return 0
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val todayStr = getString(R.string.today)
        val tommorowStr = getString(R.string.tommorow)
        val saveStr = getString(R.string.dialog_save)
        val deleteStr = getString(R.string.dialog_delete)
        val cancelStr = getString(R.string.dialog_cancel)
        var titleStr = getString(R.string.dialog_edit_title)
        if (addMode) {
            titleStr = getString(R.string.dialog_add_title)
        }

        binding = DialogAlarmEditBinding.inflate(LayoutInflater.from(context))
        timePicker = TimePickerHelper.build(alarm.hour(), alarm.minute())

        // tfTime
        timePicker.addOnPositiveButtonClickListener {
            setTime(timePicker.hour, timePicker.minute)
        }
        binding.tfTime.editText?.setOnClickListener {
            timePicker.show(parentFragmentManager, "timePickerEdit")
        }
        binding.tfTime.editText?.setText(alarm.timeStr())

        // tfDay
        val items = listOf(todayStr, tommorowStr)
        val dayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, items)
        val autoCompleteView = binding.tfDay.editText as? AutoCompleteTextView
        autoCompleteView!!.setAdapter(dayAdapter)
        autoCompleteView!!.setText(dayAdapter!!.getItem(getItemPosition()).toString(), false)

        val setDay = fun () {
            val dayStr = autoCompleteView.text.toString()
            val today = LocalDateTime.now()
            if ( dayStr == todayStr ) {
                alarm.setMonthAndDay(today)
            } else if ( dayStr == tommorowStr ) {
                val tommorow = today.plusDays(1)
                alarm.setMonthAndDay(tommorow)
            } else {
                Log.e(lTag, "dayStr unknown value: ${dayStr}")
                alarm.setMonthAndDay(today) // defaulting to today in case of a bug
            }
            Log.d(lTag, "day set to ${alarm.day()}")
        }

        // tfName
        binding.tfName.editText?.setText(alarm.name)

        return MaterialAlertDialogBuilder(requireContext(), theme).apply {
            setView(binding.root)
            setTitle(titleStr)
            setPositiveButton(saveStr) { dialog, _ ->
                alarm.name = binding.tfName.editText!!.text.toString()
                setDay()
                onAlarmSaved(alarm)
                dialog.dismiss()
            }
            if (addMode) {
                setNegativeButton(cancelStr) { dialog, _ ->
                    onAlarmCanceled(alarm)
                    dialog.dismiss()
                }
            } else {
                setNeutralButton(deleteStr) { dialog, _ ->
                    onAlarmDeleted(alarm)
                    dialog.dismiss()
                }
            }
        }.create()
    }

}
