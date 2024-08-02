package com.example.eventsmanagementapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_participant")
data class EventParticipant(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val eventId: Long,
    val participantId: Long
)
