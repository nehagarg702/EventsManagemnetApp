package com.example.eventsmanagementapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) val eventId: Long = 0,
    val eventName: String,
    val eventStartDateTime: Date,
    val eventEndDateTime: Date,
    val eventLocation: String,
    val eventDescription: String,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)


