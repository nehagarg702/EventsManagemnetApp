package com.example.eventsmanagementapp.di

import androidx.room.Room
import com.example.eventsmanagementapp.data.local.AppDatabase
import com.example.eventsmanagementapp.data.repository.EventRepository
import com.example.eventsmanagementapp.data.repository.EventRepositoryImpl
import com.example.eventsmanagementapp.util.DummyDataLoader
import com.example.eventsmanagementapp.ui.viewmodel.BaseViewModel
import com.example.eventsmanagementapp.ui.viewmodel.EventViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Room.databaseBuilder(get(), AppDatabase::class.java, "app_database").build() }
    single { get<AppDatabase>().eventDao() }
    single { get<AppDatabase>().participantDao() }
    single { get<AppDatabase>().eventParticipantDao() }
    single<EventRepository> { EventRepositoryImpl(get(), get(), get()) }
    viewModel { BaseViewModel(get()) }
    viewModel { EventViewModel(get()) }
    single { DummyDataLoader(get(), get()) }
}
