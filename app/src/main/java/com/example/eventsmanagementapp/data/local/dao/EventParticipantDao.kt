package com.example.eventsmanagementapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.eventsmanagementapp.data.local.model.EventParticipant

@Dao
interface EventParticipantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEventParticipants(eventParticipants: List<EventParticipant>)

    @Query("DELETE FROM event_participant WHERE eventId = :eventId")
    suspend fun removeParticipantsFromEvent(eventId: Long)

    @Query("DELETE FROM event_participant WHERE eventId = :eventId AND participantId = :participantId")
    suspend fun removeParticipantFromEvent(eventId: Long, participantId: Long)

    @Query("SELECT * FROM event_participant WHERE eventId = :eventId")
    suspend fun getParticipantsForEvent(eventId: Long): List<EventParticipant>

    @Query("SELECT * FROM event_participant WHERE participantId = :participantId")
    suspend fun getEventsForParticipant(participantId: Long): List<EventParticipant>

    @Query(" SELECT participantId from event_participant WHERE eventId IN (:eventIds)")
    suspend fun getParticipantsForEvents(eventIds: List<Long>): List<Long>

    @Query("SELECT * FROM event_participant")
    suspend fun getAllData(): List<EventParticipant>

}

