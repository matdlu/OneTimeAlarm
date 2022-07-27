package pl.ascendit.onetimealarm.data.alarm

import android.content.Context
import androidx.room.*
import pl.ascendit.onetimealarm.data.converter.LocalDateTimeConverter

@Database(
    entities = [Alarm::class],
    version = 1,
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AlarmDatabase : RoomDatabase() {
    abstract val alarms: AlarmDao

    companion object {
        fun open(context: Context): AlarmDatabase = Room.databaseBuilder(
            context, AlarmDatabase::class.java, "alarm.db"
        ).build()
    }

}
