package pl.ascendit.onetimealarm.data.alarm

import androidx.room.*

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarm")
    fun getAll() : List<Alarm>

    @Query("SELECT * FROM alarm WHERE id=:id ")
    fun get(id: Long) : Alarm

    @Insert
    fun add(alarm: Alarm)

    @Update
    fun update(alarm: Alarm)

    @Delete
    fun delete(alarm: Alarm)
}
