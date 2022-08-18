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

package pl.ascendit.onetimealarm.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.ascendit.onetimealarm.R
import pl.ascendit.onetimealarm.able.Updatable
import pl.ascendit.onetimealarm.data.alarm.Alarm
import pl.ascendit.onetimealarm.databinding.AlarmItemBinding
import pl.ascendit.onetimealarm.helper.TimeHelper
import pl.ascendit.onetimealarm.logic.AlarmLogic

class AlarmItemViewHolder(val binding: AlarmItemBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(alarm: Alarm, dayStr: String) {
        binding.tvTime.text = alarm.time24()
        binding.tvDay.text = dayStr
        binding.tvName.text = alarm.name
    }
}

class AlarmItemAdapter : RecyclerView.Adapter<AlarmItemViewHolder>(), Updatable {
    var data = mutableListOf<Alarm>()
    var onAlarmAdded:  (alarm : Alarm) -> Unit = {}
    var onAlarmRemoved: (alarm : Alarm) -> Unit = {}
    var onAlarmReplaced: (alarm : Alarm) -> Unit = {}
    var onAlarmsReplaced = {}
    var onAlarmBinded: (alarm: Alarm) -> Unit = {}
    var onItemClicked: (alarm: Alarm) -> Unit = {}
    private lateinit var todayStr: String
    private lateinit var tommorowStr: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmItemViewHolder {
        val binding = AlarmItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        todayStr = parent.context.getString(R.string.today)
        tommorowStr = parent.context.getString(R.string.tommorow)
        return AlarmItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmItemViewHolder, position: Int) {
        val alarm = data[position]
        onAlarmBinded(alarm)
        holder.bind(alarm, dayStr(alarm))
        holder.itemView.setOnClickListener {
            onItemClicked(alarm)
        }
    }

    private fun dayStr(alarm: Alarm) : String {
        if ( TimeHelper.isToday(alarm.datetime) ) {
            return todayStr
        } else if ( TimeHelper.isTommorow(alarm.datetime) ) {
            return tommorowStr
        } else {
            return "REPORT A BUG ${alarm.datetime.toLocalDate().toString()}"
        }
    }

    override fun getItemCount(): Int = data.size

    fun get(id: Long) : Alarm? {
        return data.find { it.id == id }
    }

    override fun replace(alarms: List<Alarm>) {
        data.clear()
        data.addAll(alarms)
        onAlarmsReplaced()
        sort()
        notifyDataSetChanged()
    }

    override fun replace(alarm: Alarm) {
        val index = data.indexOf(alarm)
        data.removeAt(index)
        data.add(index, alarm)
        onAlarmReplaced(alarm)
        sort()
        notifyDataSetChanged()
    }

    override fun add(alarm: Alarm) {
        data.add(alarm)
        onAlarmAdded(alarm)
        sort()
        notifyDataSetChanged()
    }

    override fun remove(alarm: Alarm) {
        data.remove(alarm)
        onAlarmRemoved(alarm)
        notifyDataSetChanged()
    }

    private fun sort() {
        data.sortBy { it.datetime }
    }
}
