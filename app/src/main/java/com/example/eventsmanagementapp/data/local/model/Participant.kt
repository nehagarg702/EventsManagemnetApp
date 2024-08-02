package com.example.eventsmanagementapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "participants")
data class Participant(
    @PrimaryKey(autoGenerate = true) val participantId: Long = 0,
    var participantName: String
)
