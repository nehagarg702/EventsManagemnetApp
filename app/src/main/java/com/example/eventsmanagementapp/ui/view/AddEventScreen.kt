package com.example.eventsmanagementapp.ui.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventsmanagementapp.ui.viewmodel.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(
    viewModel: EventViewModel = viewModel(),
    onEventAdded: () -> Unit,
    eventId: Long? = null,
    onSelectParticipantsClick: () -> Unit
) {
    val context = LocalContext.current
    val save = rememberSaveable { mutableStateOf(true) }
    val event by viewModel.event.observeAsState()
    val participantsForEvent by viewModel.participantsForEvent.observeAsState(emptyList())

    if (save.value) {
        LaunchedEffect(event) {
            event?.let {
                viewModel.eventName = it.eventName
                viewModel.startDateTime = TextFieldValue(viewModel.fromDate(it.eventStartDateTime).toString())
                viewModel.endDateTime = TextFieldValue(viewModel.fromDate(it.eventEndDateTime).toString())
                viewModel.location = it.eventLocation
                viewModel.description = it.eventDescription
            }
        }

        LaunchedEffect(participantsForEvent) {
            viewModel.selectedParticipant = participantsForEvent.toMutableStateList()
            viewModel.updateSelectedParticipants()
        }
        save.value = false
    }

    LaunchedEffect(viewModel.startDateTime, viewModel.endDateTime) {
        if (!viewModel.isEndDateTimeValid(viewModel.startDateTime.text, viewModel.endDateTime.text, eventId)) {
            Toast.makeText(context, "Please select a valid date", Toast.LENGTH_SHORT).show()
            viewModel.endDateTime = TextFieldValue("")
        }
    }

    // Main Column to hold the content
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = if (eventId == null) "Add Event" else "Edit Event",
            onBackClick = { onEventAdded() },
            onRightButtonClick = {
                if (viewModel.saveEvent(eventId)) {
                    onEventAdded()
                }
            }
        )

        // LazyColumn for scrollable content
        LazyColumn(
            modifier = Modifier.weight(1f, false),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            item {
                OutlinedTextField(
                    value = viewModel.eventName,
                    onValueChange = { viewModel.eventName = it },
                    label = { Text("Event Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }

            item {
                DateTimePicker(
                    value = viewModel.startDateTime,
                    onValueChange = { viewModel.startDateTime = it },
                    label = "Start DateTime"
                )
            }

            item {
                DateTimePicker(
                    value = viewModel.endDateTime,
                    onValueChange = { viewModel.endDateTime = it },
                    label = "End DateTime"
                )
            }

            item {
                OutlinedTextField(
                    value = viewModel.location,
                    onValueChange = { viewModel.location = it },
                    label = { Text("Location") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = viewModel.description,
                    onValueChange = { viewModel.description = it },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    maxLines = 3
                )
            }

            if (viewModel.selectedParticipant.isNotEmpty()) {
                item {
                    Text(
                        "Selected Participants:",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                items(viewModel.selectedParticipant) { participant ->
                    ParticipantRow(participant = participant, onRemoveParticipant = { participant ->
                        viewModel.removeSelectedParticipant(participant)
                    })
                }
            }
        }

        Button(
            onClick = onSelectParticipantsClick,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 8.dp)
        ) {
            Text("Add Participants")
        }
    }

    viewModel.errorMessage?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        viewModel.errorMessage = null
    }
}



