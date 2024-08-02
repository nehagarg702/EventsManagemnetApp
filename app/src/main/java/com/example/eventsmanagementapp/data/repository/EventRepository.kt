package com.example.eventsmanagementapp.data.repository

import com.example.eventsmanagementapp.data.local.model.Event
import com.example.eventsmanagementapp.data.local.model.EventParticipant
import com.example.eventsmanagementapp.data.local.model.Participant
import com.example.eventsmanagementapp.util.Result
import java.util.Date

interface EventRepository {
    suspend fun insertEvent(event: Event): Result<Long>
    suspend fun updateEvent(event: Event): Result<Unit>
    suspend fun getAllEvents(): Result<List<Event>>
    suspend fun getEventById(eventId: Long): Result<Event?>

    suspend fun getParticipants(): Result<List<Participant>>
    suspend fun getParticipantsForEvent(eventId: Long): Result<List<Participant>>
    suspend fun linkParticipantsToEvent(eventId: Long, participantIds: List<Long>): Result<Unit>
    suspend fun unlinkParticipantFromEvent(eventId: Long, participantId: Long): Result<Unit>
    suspend fun getEventsForParticipant(participantId: Long): Result<List<Event>>
    suspend fun insertParticipants(participants: List<Participant>): Result<Unit>
    suspend fun clearParticipantsForEvent(eventId: Long): Result<Unit>
    suspend fun getEventsInRange(startDateTime: Date, endDateTime: Date): Result<List<Long>>
    suspend fun getParticipantsForEvents(eventIds: List<Long>): Result<List<Long>>
    suspend fun getAllEventParticipants(): Result<List<EventParticipant>>
}

