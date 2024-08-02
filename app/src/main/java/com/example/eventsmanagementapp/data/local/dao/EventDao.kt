package com.example.eventsmanagementapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.eventsmanagementapp.data.local.model.Event
import java.util.Date

@Dao
interface EventDao {

    @Query("SELECT * FROM events")
    suspend fun getAllEvents(): List<Event>

    @Query("SELECT * FROM events WHERE eventId = :eventId")
    suspend fun getEventById(eventId: Long): Event?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event): Long

    @Update
    suspend fun updateEvent(event: Event): Int

    @Query(" SELECT eventId FROM events WHERE (eventStartDateTime < :endDateTime AND eventEndDateTime > :startDateTime)")
    suspend fun getEventsInRange(startDateTime: Date, endDateTime: Date): List<Long>
}
