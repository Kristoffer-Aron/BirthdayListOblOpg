package com.example.birthdaylistoblopg.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.birthdaylistoblopg.PersonUIState
import com.example.birthdaylistoblopg.data.NetworkResult
import com.example.birthdaylistoblopg.data.Person
import com.example.birthdaylistoblopg.data.PersonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PersonViewModel(
    private val personRepository: PersonRepository // dependency injection
) : ViewModel() {
    private val _personUIState = MutableStateFlow(PersonUIState())
    val personUIState: StateFlow<PersonUIState> = _personUIState

    private var originalPersonList: List<Person> = emptyList()

    fun clearPersons() {
        _personUIState.update { PersonUIState() }
    }

    fun getPersons(userId: String) {
        Log.d("PersonViewModel", "getPersons called with userId: $userId")
        _personUIState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            when (val result = personRepository.getPersons(userId)) {
                is NetworkResult.Success -> {
                    originalPersonList = result.data
                    _personUIState.update { uiState ->
                        uiState.copy(isLoading = false, persons = result.data)
                    }
                }

                is NetworkResult.Error -> {
                    _personUIState.update {
                        it.copy(isLoading = false, error = result.error)
                    }
                }
            }
        }
    }

    fun addPerson(person: Person) {
        _personUIState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (val result = personRepository.addPerson(person)) {
                is NetworkResult.Success -> {
                    getPersons(person.userId)
                }
                is NetworkResult.Error -> {
                    _personUIState.update {
                        it.copy(isLoading = false, error = result.error)
                    }
                }
            }
        }
    }
}
