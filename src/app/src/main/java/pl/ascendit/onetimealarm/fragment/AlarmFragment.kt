package pl.ascendit.onetimealarm.fragment

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.timepicker.MaterialTimePicker
import pl.ascendit.onetimealarm.able.Loadable
import pl.ascendit.onetimealarm.adapter.AlarmItemAdapter
import pl.ascendit.onetimealarm.data.alarm.Alarm
import pl.ascendit.onetimealarm.data.alarm.AlarmDatabase
import pl.ascendit.onetimealarm.databinding.FragmentAlarmBinding
import pl.ascendit.onetimealarm.dialog.DialogAlarmEdit
import pl.ascendit.onetimealarm.helper.TimeHelper
import pl.ascendit.onetimealarm.helper.TimePickerHelper
import pl.ascendit.onetimealarm.logic.AlarmLogic
import java.time.LocalDateTime
import kotlin.concurrent.thread

class AlarmFragment : Fragment(), Loadable {
    val lTag = "AlarmFragment"
    private lateinit var binding: FragmentAlarmBinding
    private lateinit var alarmDatabase: AlarmDatabase
    private lateinit var timePicker: MaterialTimePicker
    var adapter: AlarmItemAdapter? = null
    var onAdapterCreated: (adapter : AlarmItemAdapter) -> Unit = {}
    var btAddLastClickTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        alarmDatabase = AlarmDatabase.open(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentAlarmBinding.inflate(inflater, container, false).also {
            container!!.removeAllViews()
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val funNoAlarms = {
            binding.tvNoAlarms.visibility = if ( adapter!!.itemCount <= 0 ) View.VISIBLE else View.GONE
        }

        adapter = AlarmItemAdapter().apply {
            onAlarmAdded = { funNoAlarms() }
            onAlarmRemoved = { funNoAlarms() }
            onAlarmsReplaced = { funNoAlarms() }
            onItemClicked = {
                val dialog = DialogAlarmEdit(it)
                dialog.onAlarmSaved = {
                    AlarmLogic.updateAlarm(requireContext(), it)
                }
                dialog.onAlarmDeleted = {
                    AlarmLogic.deleteAlarm(requireContext(), it)
                }

                dialog.show(parentFragmentManager, "dialogAlarmEdit")
            }
            onAlarmBinded = {
                if ( ! (TimeHelper.isToday(it.datetime) || TimeHelper.isTommorow(it.datetime)) ) {
                    Log.e("ALARM NOT FIRED", "deleting alarm ${it}")
                    AlarmLogic.deleteAlarm(requireContext(), it)
                }
            }
        }

        onAdapterCreated(adapter!!)

        loadData()

        binding.list.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }

        timePicker = TimePickerHelper.build()
        timePicker.addOnPositiveButtonClickListener {
            var alarm = Alarm()
            alarm.setTime(timePicker.hour, timePicker.minute)
            if ( alarm.datetime.isBefore(LocalDateTime.now()) ) {
                alarm.setMonthAndDay(LocalDateTime.now().plusDays(1))
            }
            val dialog = DialogAlarmEdit(alarm)
            dialog.addMode = true
            dialog.onAlarmSaved = {
                AlarmLogic.addAlarm(requireContext(), it)
            }
            dialog.show(parentFragmentManager, "dialogAlarmAdd")
        }

        binding.btAdd.setOnClickListener {
            if ( ! ( SystemClock.elapsedRealtime() - btAddLastClickTime < 1000 )){ // prevent double click
                btAddLastClickTime = SystemClock.elapsedRealtime()
                timePicker.show(parentFragmentManager, "timePickerAdd")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    override fun loadData() {
        thread {
            val data = alarmDatabase.alarms.getAll()
            requireActivity().runOnUiThread() {
                adapter?.replace(data)
            }
        }
    }
}
