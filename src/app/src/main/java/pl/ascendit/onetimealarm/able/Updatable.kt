package pl.ascendit.onetimealarm.able

import pl.ascendit.onetimealarm.data.alarm.Alarm

interface Updatable {
    fun replace(alarms: List<Alarm>)

    fun replace(alarm: Alarm)

    fun add(alarm: Alarm)

    fun remove(alarm: Alarm)
}