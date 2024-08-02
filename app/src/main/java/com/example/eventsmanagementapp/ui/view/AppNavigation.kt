package com.example.eventsmanagementapp.ui.view

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.eventsmanagementapp.ui.viewmodel.EventViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: EventViewModel = getViewModel()

    NavHost(navController = navController, startDestination = "eventList") {
        composable("eventList") {
            EventListScreen(
                viewModel = viewModel,
                onEventClick = { eventId ->
                    navController.navigate("eventDetail/$eventId")
                },
                onAddEventClick = {
                    viewModel.resetEventData()
                    navController.navigate("addEvent")
                },
                onEditClick = { eventId ->
                    navController.navigate("editEvent/$eventId")
                }
            )
        }
        composable("eventDetail/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")?.toLong() ?: 0L
            EventDetailScreen(
                viewModel = viewModel,
                eventId = eventId,
                onBackClick = {
                    navController.popBackStack()
                    viewModel.setEventValueNull() },
                onEditClick = {
                    navController.popBackStack()
                    navController.navigate("editEvent/$eventId")
                }
            )
        }
        composable("addEvent") {
            val eventId = null
            AddEventScreen(
                viewModel = viewModel,
                onEventAdded = {
                    navController.popBackStack()
                },
                onSelectParticipantsClick = {
                    navController.navigate("participantSelection/$eventId")
                }
            )
        }
        composable("editEvent/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")?.toLong() ?: 0L
            viewModel.loadEvent(eventId)
            viewModel.loadParticipantsForEvent(eventId)
            AddEventScreen(
                viewModel = viewModel,
                onEventAdded = { navController.popBackStack() },
                eventId = eventId,
                onSelectParticipantsClick = {
                    navController.navigate("participantSelection/$eventId")
                }
            )
        }
        composable("participantSelection/{eventId}") {backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            ?.let {
                if(it == "null") null
                else it.toLong()
            }
            ParticipantSelectionScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                eventId = eventId
            )
        }
    }
}


