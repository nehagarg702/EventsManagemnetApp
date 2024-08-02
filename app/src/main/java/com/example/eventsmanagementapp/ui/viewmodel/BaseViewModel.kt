package com.example.eventsmanagementapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eventsmanagementapp.data.repository.EventRepository
import com.example.eventsmanagementapp.data.repository.EventRepositoryImpl

open class BaseViewModel(
    eventRepository: EventRepository
) : ViewModel() {

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    init {
        if (eventRepository is EventRepositoryImpl) {
            eventRepository.error.observeForever {
                _error.postValue(it)
            }
        }
    }

    protected fun setError(errorMessage: String?) {
        _error.postValue(errorMessage)
    }

    fun clearError() {
        _error.postValue(null)
    }
}
