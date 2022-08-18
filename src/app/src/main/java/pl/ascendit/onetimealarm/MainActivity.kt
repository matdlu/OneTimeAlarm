package pl.ascendit.onetimealarm

import android.app.AlarmManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import pl.ascendit.onetimealarm.able.Navigable
import pl.ascendit.onetimealarm.data.alarm.AlarmDatabase
import pl.ascendit.onetimealarm.fragment.AboutFragment
import pl.ascendit.onetimealarm.fragment.AlarmFragment
import pl.ascendit.onetimealarm.fragment.SettingsFragment
import pl.ascendit.onetimealarm.logic.AlarmLogic
import pl.ascendit.onetimealarm.logic.SettingsLogic
import pl.ascendit.onetimealarm.logic.SoundLogic
import pl.ascendit.onetimealarm.receiver.NotificationReceiver

class MainActivity : AppCompatActivity(), Navigable {
    private lateinit var alarmFragment: AlarmFragment
    private lateinit var settingsFragment: SettingsFragment
    private var aboutFragment: AboutFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        NotificationReceiver.createNotificationChannel(baseContext)
        SoundLogic.init(baseContext)
        SettingsLogic.load(baseContext)

        AlarmLogic.alarmDatabase = AlarmDatabase.open(baseContext)
        AlarmLogic.alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        alarmFragment = AlarmFragment()
        alarmFragment.onAdapterCreated = {
            AlarmLogic.updatable = it
        }

        AlarmLogic.loadable = alarmFragment

        settingsFragment = SettingsFragment()
        settingsFragment.register(this)

        navigate(Navigable.Destination.Alarm)
    }

    // Options
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = MenuInflater(this)
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                navigate(Navigable.Destination.Settings)
                true
            }
            R.id.about -> {
                navigate(Navigable.Destination.About)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Navigation
    var level = 0

    override fun navigate(to: Navigable.Destination) {
        when (to) {
            Navigable.Destination.About -> {
                if ( aboutFragment == null ) {
                    aboutFragment = AboutFragment()
                }
                goToFragment(aboutFragment!!, AboutFragment::class.java.name)
                level = 1
            }
            Navigable.Destination.Settings -> {
                goToFragment(settingsFragment, SettingsFragment::class.java.name)
                level = 1
            }
            Navigable.Destination.Alarm -> {
                goToFragment(alarmFragment, AlarmFragment::class.java.name)
                level = 0
            }
        }
    }

    fun goToFragment(fragment: Fragment, tag : String) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment, tag)
        }.commit()
    }

    override fun onBackPressed() {
        if ( level > 0 ) {
            navigate(Navigable.Destination.Alarm)
        } else {
            finish()
        }
    }
}