package com.example.eventsmanagementapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eventsmanagementapp.data.local.dao.EventDao
import com.example.eventsmanagementapp.data.local.dao.EventParticipantDao
import com.example.eventsmanagementapp.data.local.dao.ParticipantDao
import com.example.eventsmanagementapp.data.local.model.Event
import com.example.eventsmanagementapp.data.local.model.EventParticipant
import com.example.eventsmanagementapp.data.local.model.Participant
import com.example.eventsmanagementapp.util.Result
import java.util.Date

class EventRepositoryImpl(
    private val eventDao: EventDao,
    private val participantDao: ParticipantDao,
    private val eventParticipantDao: EventParticipantDao
) : EventRepository {

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private suspend fun <T> safeCall(action: suspend () -> T): Result<T> {
        return try {
            Result.Success(action())
        } catch (e: Exception) {
            _error.postValue(e.localizedMessage)
            Result.Error(e.stackTraceToString())
        }
    }

    override suspend fun insertEvent(event: Event): Result<Long> =
        safeCall { eventDao.insertEvent(event) }

    override suspend fun updateEvent(event: Event): Result<Unit> =
        safeCall {
            eventDao.updateEvent(event)
            Unit
        }

    override suspend fun getAllEvents(): Result<List<Event>> =
        safeCall { eventDao.getAllEvents() }

    override suspend fun getEventById(eventId: Long): Result<Event?> =
        safeCall { eventDao.getEventById(eventId) }

    override suspend fun getParticipants(): Result<List<Participant>> =
        safeCall { participantDao.getParticipants() }

    override suspend fun linkParticipantsToEvent(eventId: Long, participantIds: List<Long>): Result<Unit> =
        safeCall {
            val eventParticipants = participantIds.map { participantId ->
                EventParticipant(eventId = eventId, participantId = participantId)
            }
            eventParticipantDao.insertEventParticipants(eventParticipants)
        }

    override suspend fun unlinkParticipantFromEvent(eventId: Long, participantId: Long): Result<Unit> =
        safeCall {
            eventParticipantDao.removeParticipantFromEvent(eventId, participantId)
        }

    override suspend fun getParticipantsForEvent(eventId: Long): Result<List<Participant>> {
        return safeCall {
            val eventParticipants = eventParticipantDao.getParticipantsForEvent(eventId)
            val participantIds = eventParticipants.map { it.participantId }
            participantDao.getParticipants().filter { it.participantId in participantIds }
        }
    }

    override suspend fun getEventsForParticipant(participantId: Long): Result<List<Event>> {
        return safeCall {
            val eventParticipants = eventParticipantDao.getEventsForParticipant(participantId)
            val eventIds = eventParticipants.map { it.eventId }
            eventDao.getAllEvents().filter { it.eventId in eventIds }
        }
    }

    override suspend fun insertParticipants(participants: List<Participant>): Result<Unit> =
        safeCall { participantDao.insertParticipants(participants) }

    override suspend fun clearParticipantsForEvent(eventId: Long): Result<Unit> =
        safeCall {
            eventParticipantDao.removeParticipantsFromEvent(eventId)
        }

    override suspend fun getEventsInRange(startDateTime: Date, endDateTime: Date): Result<List<Long>> =
       safeCall {  eventDao.getEventsInRange(startDateTime, endDateTime) }

    override suspend fun getParticipantsForEvents(eventIds: List<Long>): Result<List<Long>> =
        safeCall {
            eventParticipantDao.getParticipantsForEvents(eventIds)
        }

    override suspend fun getAllEventParticipants(): Result<List<EventParticipant>> =
        safeCall {
            eventParticipantDao.getAllData()
        }
}
