package com.example.eventsmanagementapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import com.example.eventsmanagementapp.data.local.model.Participant

@Dao
interface ParticipantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipants(participants: List<Participant>)

    @Query("SELECT * FROM participants")
    suspend fun getParticipants(): List<Participant>
}
