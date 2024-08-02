package com.example.eventsmanagementapp.ui.view

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eventsmanagementapp.data.local.model.Event
import com.example.eventsmanagementapp.ui.viewmodel.EventViewModel
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(
    viewModel: EventViewModel = getViewModel(),
    onEventClick: (Long) -> Unit,
    onAddEventClick: () -> Unit,
    onEditClick: (Long) -> Unit
) {
    LocalContext.current
    val events by viewModel.events.observeAsState(emptyList())
    val searchQuery = remember { mutableStateOf("") }

    // Use a LaunchedEffect to load events only when the composable is first displayed
    LaunchedEffect(Unit) {
        viewModel.loadEvents()
        viewModel.loadAllParticipants()
        viewModel.loadAllEventsParticipants()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddEventClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Event")
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 0.dp)
            ) {

                TopAppBar(title = "Events Management App")

                Spacer(modifier = Modifier.height(8.dp))
                // Search Bar
                Card(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(8.dp),
                    shape = RoundedCornerShape(32.dp)
                ) {
                    TextField(
                        value = searchQuery.value,
                        onValueChange = { query ->
                            searchQuery.value = query
                            viewModel.loadEvents()
                        },
                        modifier = Modifier.fillMaxWidth().focusable(false),
                        textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
                        placeholder = { Text("Search...", style = TextStyle(fontSize = 18.sp, color = Color.Gray)) },
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "search") },
                        trailingIcon = {
                            if (searchQuery.value.isNotEmpty()) {
                                IconButton(onClick = { searchQuery.value = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear")
                                }
                            }
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color(0XFFE8E9EB),
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.White
                        ),
                        maxLines = 1
                    )
                }

                // Display events or no events message
                if (events.isEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "No events available. Click the add button to create an event.",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                } else {
                    val filteredEvents = events.filter {
                        it.eventName.contains(searchQuery.value, ignoreCase = true) ||
                                it.eventDescription.contains(searchQuery.value, ignoreCase = true)
                    }
                    if (filteredEvents.isEmpty()) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "No results found.",
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                    } else {
                        EventList(filteredEvents, onEventClick, modifier = Modifier.padding(paddingValues), onEditClick, viewModel)
                    }                }
            }
        }
    )
}

@Composable
fun EventList(
    events: List<Event>,
    onEventClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    onEditClick: (Long) -> Unit,
    viewModel: EventViewModel
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        items(events) { event ->
            EventItem(event = event, onEventClick = onEventClick, onEditClick,viewModel)
        }
    }
}

@Composable
fun EventItem(
    event: Event,
    onEventClick: (Long) -> Unit,
    onEditClick: (Long) -> Unit,
    viewModel: EventViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onEventClick(event.eventId) },
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = event.eventName,
                    style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.primary)
                )

                // Location with icon
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = "Location", tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = event.eventLocation,
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
                    )
                }

                // Date and time with icon
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DateRange, contentDescription = "Date and Time", tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${viewModel.fromDate(event.eventStartDateTime)} - ${viewModel.fromDate(event.eventEndDateTime)}",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black)
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Text(
                    text = viewModel.formatEventDateTime(event.updatedAt),
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
                )

                IconButton(
                    onClick = { onEditClick(event.eventId) },
                    modifier = Modifier.align(Alignment.End),
                    content = {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Event", tint = MaterialTheme.colorScheme.primary)
                    }
                )
            }
        }
    }
}

