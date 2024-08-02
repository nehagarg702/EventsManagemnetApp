package com.example.eventsmanagementapp.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.eventsmanagementapp.data.local.model.Participant
import com.example.eventsmanagementapp.ui.viewmodel.EventViewModel

@Composable
fun EventDetailScreen(
    viewModel: EventViewModel,
    eventId: Long,
    onBackClick: () -> Unit,
    onEditClick: (Long) -> Unit
) {
    val eventData by viewModel.event.observeAsState()
    val participants by viewModel.participantsForEvent.observeAsState(emptyList())

    LaunchedEffect(eventId) {
        viewModel.loadEvent(eventId)
        viewModel.loadParticipantsForEvent(eventId)
    }

    eventData?.let { event ->
        Column {
            TopAppBar(
                title = event.eventName,
                onBackClick = { onBackClick() },
                rightIconImageVector = Icons.Default.Edit, // Use Edit icon
                onRightButtonClick = { onEditClick(event.eventId) } // Pass event ID to edit
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
            ){
                item {
                    Row(modifier = Modifier
                        .fillMaxWidth().padding(bottom = 8.dp)) {
                        Icon(Icons.Default.LocationOn, contentDescription = "Location", tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = event.eventLocation, style = MaterialTheme.typography.bodyLarge)
                    }
                }

                item {
                    // Display Event Date and Time
                    Row(modifier = Modifier
                        .fillMaxWidth().padding(vertical = 8.dp)) {
                        Icon(Icons.Default.DateRange, contentDescription = "Date and Time", tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "${viewModel.fromDate(event.eventStartDateTime)} - ${viewModel.fromDate(event.eventEndDateTime)}", style = MaterialTheme.typography.bodyLarge)
                    }
                }

                item {
                    // Display Participants Header
                    Text(text = "Description: ", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(top = 8.dp))
                }

                item {
                    // Display Event Description
                    Text(text = event.eventDescription, style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp, bottom = 16.dp))
                }

                item {
                    // Display Participants Header
                    Text(text = "Participants: ", style = MaterialTheme.typography.headlineSmall)
                }

                // List of Participants
                items(participants) { participant ->
                    ParticipantListItem(participant)
                }
            }

            // List of Participants
        }
    }
}


@Composable
fun ParticipantListItem(participant: Participant) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ParticipantIcon(participantName = participant.participantName)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = participant.participantName, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
        }
    }
}

