package com.example.eventsmanagementapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.eventsmanagementapp.data.local.model.Event
import com.example.eventsmanagementapp.data.local.model.EventParticipant
import com.example.eventsmanagementapp.data.local.model.Participant
import com.example.eventsmanagementapp.data.repository.EventRepository
import com.example.eventsmanagementapp.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EventViewModel(private val eventRepository: EventRepository) : BaseViewModel(eventRepository) {

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> get() = _events

    private val _event = MutableLiveData<Event?>()
    val event: LiveData<Event?> get() = _event

    private val _participantsForEvent = MutableLiveData<List<Participant>>()
    val participantsForEvent: LiveData<List<Participant>> get() = _participantsForEvent

    private val _participants = MutableLiveData<List<Participant>>()
    val participants: LiveData<List<Participant>> get() = _participants

    private val _selectedParticipants = MutableLiveData<List<Participant>>()
    val selectedParticipants: LiveData<List<Participant>> get() = _selectedParticipants

    private val _eventParticipants = MutableLiveData<List<EventParticipant>>()
    val eventParticipant: LiveData<List<EventParticipant>> get() = _eventParticipants

    // Observable states
    var eventName by mutableStateOf("")
    var startDateTime by mutableStateOf(TextFieldValue(""))
    var endDateTime by mutableStateOf(TextFieldValue(""))
    var location by mutableStateOf("")
    var description by mutableStateOf("")
    var selectedParticipant = mutableStateListOf<Participant>()
    var errorMessage by mutableStateOf<String?>(null)

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm a", Locale.getDefault())

    fun loadEvents() {
        viewModelScope.launch {
            when (val result = eventRepository.getAllEvents()) {
                is Result.Success -> _events.postValue(result.data)
                is Result.Error -> setError(result.message)
            }
        }
    }

    fun loadAllParticipants() {
        viewModelScope.launch {
            when (val result = eventRepository.getParticipants()) {
                is Result.Success -> _participants.postValue(result.data)
                is Result.Error -> setError(result.message)
            }
        }
    }

    fun loadAllEventsParticipants() {
        viewModelScope.launch {
            when (val result = eventRepository.getAllEventParticipants()) {
                is Result.Success -> _eventParticipants.postValue(result.data)
                is Result.Error -> {}
            }
        }
    }

    fun loadEvent(eventId: Long) {
        val event = _events.value?.filter { it.eventId == eventId }
        event?.let {
            if(it.isNotEmpty()){
                _event.postValue(it[0])
                return
            }
        }
        viewModelScope.launch {
            when (val result = eventRepository.getEventById(eventId)) {
                is Result.Success -> _event.postValue(result.data)
                is Result.Error -> setError(result.message)
            }
        }
    }

    private fun addEvent(event: Event) {
        viewModelScope.launch {
            when (val result = eventRepository.insertEvent(event)) {
                is Result.Success -> {
                    val newEventId = result.data
                    saveSelectedParticipants(newEventId)
                }
                is Result.Error -> setError(result.message)
            }
        }
    }

    private fun updateEvent(event: Event) {
        viewModelScope.launch {
            when (val result = eventRepository.updateEvent(event)) {
                is Result.Success -> {
                    saveSelectedParticipants(event.eventId)  // Save participants
                }
                is Result.Error -> setError(result.message)
            }
        }
    }

    private suspend fun saveSelectedParticipants(eventId: Long) {
        viewModelScope.launch {
            eventRepository.clearParticipantsForEvent(eventId)
            val participantIds = selectedParticipant.map { it.participantId }
            eventRepository.linkParticipantsToEvent(eventId, participantIds)
            loadEvents()
            loadAllParticipants()
            loadAllEventsParticipants()
        }
    }

    fun loadParticipantsForEvent(eventId: Long) {
        val participantIds = _eventParticipants.value?.filter { it.eventId == eventId }?.map { it.participantId }
        participantIds?.let {participantId ->
            val participants = _participants.value?.filter { participantId.contains(it.participantId) }
            participants?.let {
                if(it.isNotEmpty()){
                    _participantsForEvent.postValue(it)
                    return
                }
            }
        }
        viewModelScope.launch {
            when (val result = eventRepository.getParticipantsForEvent(eventId)) {
                is Result.Success -> _participantsForEvent.postValue(result.data)
                is Result.Error -> setError(result.message)
            }
        }
    }

    fun loadParticipants(eventId: Long?) {
        viewModelScope.launch {
            when (val result = eventRepository.getParticipants()) {
                is Result.Success -> {
                    val allParticipants = result.data
                    val collidingParticipantIds = withContext(Dispatchers.IO) {
                        checkForTimeCollisions(startDateTime.text, endDateTime.text, allParticipants,eventId)
                    }
                    val filteredParticipants = allParticipants.filter { participant ->
                        !collidingParticipantIds.contains(participant.participantId)
                    }
                    _participants.postValue(filteredParticipants)
                }
                is Result.Error -> setError(result.message)
            }
        }
    }

    private fun updateSelectedParticipants(participants: List<Participant>, eventId: Long?) {
        viewModelScope.launch {
            val collidingParticipantIds = withContext(Dispatchers.IO) {
                checkForTimeCollisions(startDateTime.text, endDateTime.text, participants, eventId)
            }

            if(collidingParticipantIds.isNotEmpty()){
                val filteredParticipants = participants.filter { participant ->
                    !collidingParticipantIds.contains(participant.participantId)
                }
                selectedParticipant.clear()
                selectedParticipant.addAll(filteredParticipants)
                _selectedParticipants.value = filteredParticipants
                errorMessage = "Removed the selected Participants who are colliding with some other event."
            }
        }
    }

    fun removeSelectedParticipant(participant: Participant) {
        selectedParticipant.remove(participant)
        _selectedParticipants.value = selectedParticipant
    }

    fun updateSelectedParticipants(){
        _selectedParticipants.value = selectedParticipant
    }

    private suspend fun checkForTimeCollisions(
        startDateTime: String,
        endDateTime: String,
        selectedParticipants: List<Participant>,
        eventId: Long? = null
    ): List<Long> {
        if (startDateTime.isBlank() || endDateTime.isBlank() || startDateTime.length < 16 || endDateTime.length < 16) return emptyList()
        if (selectedParticipants.isEmpty()) return emptyList()

        when (val result = eventRepository.getEventsInRange(dateFormat.parse(startDateTime), dateFormat.parse(endDateTime))){
            is Result.Success -> {
                val eventIds = eventId?.let { result.data.minus(it) } ?: result.data
                when (val participantsForEventsResult = eventRepository.getParticipantsForEvents(eventIds)){
                    is Result.Success -> {
                        val selectedParticipantIds = selectedParticipants.map { it.participantId }.toSet()
                        val commonParticipantIds = participantsForEventsResult.data.intersect(selectedParticipantIds)
                        return commonParticipantIds.toList()
                    }
                    is Result.Error -> setError(participantsForEventsResult.message)
                }
            }
            is Result.Error -> setError(result.message)
        }
        return emptyList()
    }

    fun isEndDateTimeValid(startDateTime: String, endDateTime: String, eventId: Long?): Boolean {
        if (startDateTime.isBlank() || endDateTime.isBlank() || startDateTime.length < 16 || endDateTime.length < 16) return true

        val startDate = dateFormat.parse(startDateTime)
        val endDate = dateFormat.parse(endDateTime)

        return if (startDate.before(endDate)) {
            if(selectedParticipant.isNotEmpty()) {
                viewModelScope.launch {
                    updateSelectedParticipants(selectedParticipant, eventId)
                }
            }
            true
        } else false
    }

    fun saveEvent(eventId: Long?): Boolean {
        if (eventName.isNotBlank() && startDateTime.text.isNotBlank() &&
            endDateTime.text.isNotBlank() && location.isNotBlank() &&
            description.isNotBlank() && selectedParticipant.isNotEmpty() &&
            errorMessage == null
        ) {
            val newEvent = Event(
                eventId = eventId ?: 0L,
                eventName = eventName,
                eventStartDateTime = dateFormat.parse(startDateTime.text),
                eventEndDateTime = dateFormat.parse(endDateTime.text),
                eventLocation = location,
                eventDescription = description,
                createdAt = event.value?.createdAt ?: Date(),
                updatedAt = Date()
            )
            if (eventId == null) {
                addEvent(newEvent)
            } else {
                updateEvent(newEvent)
            }
            return true
        } else {
            if (errorMessage == null)
                errorMessage = "Please Fill all Details.."
            return false
        }
    }

    fun resetEventData() {
        setEventValueNull()
        eventName = ""
        startDateTime = TextFieldValue("")
        endDateTime = TextFieldValue("")
        location = ""
        description = ""
        selectedParticipant.clear()
        _selectedParticipants.value = emptyList()
        _participantsForEvent.value = emptyList()
        errorMessage = null
    }

    fun setEventValueNull(){
        _event.postValue(null)
    }

    fun fromDate(date: Date?): String? {
        return date?.let { dateFormat.format(it) }
    }

    fun formatEventDateTime(eventDateTime: Date): String {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        val eventDate = dateFormat.format(eventDateTime)
        val todayDate = dateFormat.format(currentDate)

        val calendar = Calendar.getInstance().apply { time = currentDate }
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val yesterdayDate = dateFormat.format(calendar.time)

        return when (eventDate) {
            todayDate -> timeFormat.format(eventDateTime)
            yesterdayDate -> "Yesterday"
            else -> dateFormat.format(eventDateTime)
        }
    }
}
