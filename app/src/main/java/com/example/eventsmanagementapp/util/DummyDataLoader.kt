package com.example.eventsmanagementapp.util

import android.content.Context
import com.example.eventsmanagementapp.data.local.model.Participant
import com.example.eventsmanagementapp.data.repository.EventRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DummyDataLoader(
    private val context: Context,
    private val eventRepository: EventRepository
) {

    fun loadDummyDataIfFirstRun() {
        if (PreferenceHelper.isFirstLaunch(context)) {
            CoroutineScope(Dispatchers.IO).launch {
                loadDummyParticipants()
            }
            PreferenceHelper.setFirstLaunch(context, false)
        }
    }

    private suspend fun loadDummyParticipants() {
        val dummyParticipants = listOf(
            Participant(participantName = "John Doe"),
            Participant(participantName = "Jane Smith"),
            Participant(participantName = "Alice Johnson"),
            Participant(participantName = "Bob Brown"),
            Participant(participantName = "Chris Martin"),
            Participant(participantName = "David Lee"),
            Participant(participantName = "Emma Davis"),
            Participant(participantName = "Fiona Harris"),
            Participant(participantName = "George Clark"),
            Participant(participantName = "Hannah Lewis"),
            Participant(participantName = "Ivy Young"),
            Participant(participantName = "Jack King"),
            Participant(participantName = "Katie Wright"),
            Participant(participantName = "Liam Turner"),
            Participant(participantName = "Mia Scott"),
            Participant(participantName = "Noah Hill"),
            Participant(participantName = "Olivia Green"),
            Participant(participantName = "Peter Adams"),
            Participant(participantName = "Quinn Mitchell"),
            Participant(participantName = "Rachel Evans")
        )
        eventRepository.insertParticipants(dummyParticipants)
    }

}
