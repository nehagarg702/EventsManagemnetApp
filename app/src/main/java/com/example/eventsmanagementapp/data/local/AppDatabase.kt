package com.example.eventsmanagementapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.eventsmanagementapp.data.local.dao.EventDao
import com.example.eventsmanagementapp.data.local.dao.EventParticipantDao
import com.example.eventsmanagementapp.data.local.dao.ParticipantDao
import com.example.eventsmanagementapp.data.local.model.Event
import com.example.eventsmanagementapp.data.local.model.EventParticipant
import com.example.eventsmanagementapp.data.local.model.Participant
import com.example.eventsmanagementapp.util.DateConverter

@Database(entities = [Event::class, Participant::class, EventParticipant::class], version = 2, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun participantDao(): ParticipantDao
    abstract fun eventParticipantDao(): EventParticipantDao
}