package com.example.eventsmanagementapp.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.eventsmanagementapp.ui.viewmodel.EventViewModel

@Composable
fun ParticipantSelectionScreen(
    viewModel: EventViewModel,
    onBackClick: () -> Unit,
    eventId : Long?
) {
    // Retrieve state from ViewModel
    val participants by viewModel.participants.observeAsState(emptyList())
    val selectedParticipants by viewModel.selectedParticipants.observeAsState(emptyList())

    // State for currently selected participants in the UI
    var selectedParticipantsSet by remember { mutableStateOf(selectedParticipants.toMutableSet()) }

    // Load participants when the screen is first composed
    LaunchedEffect(Unit) {
        viewModel.loadParticipants(eventId)
    }

    // Synchronize selectedParticipantsSet with the selectedParticipants from ViewModel
    LaunchedEffect(selectedParticipants) {
        selectedParticipantsSet = selectedParticipants.toMutableSet()
    }

    // Update selected participants in ViewModel when the right button is clicked
    fun updateAndNavigate() {
        viewModel.selectedParticipant.clear()
        viewModel.selectedParticipant.addAll(selectedParticipantsSet.toList())
        viewModel.updateSelectedParticipants()
        onBackClick()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = "Select Participants",
                onBackClick = { onBackClick() },
                onRightButtonClick = { updateAndNavigate() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)) {
                items(participants) { participant ->
                    val isSelected = selectedParticipantsSet.contains(participant)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                selectedParticipantsSet = if (isSelected) {
                                    selectedParticipantsSet
                                        .minus(participant)
                                        .toMutableSet()
                                } else {
                                    selectedParticipantsSet
                                        .plus(participant)
                                        .toMutableSet()
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ParticipantIcon(participantName = participant.participantName)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            participant.participantName,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}
