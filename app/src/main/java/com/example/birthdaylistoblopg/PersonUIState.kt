package com.example.birthdaylistoblopg

import com.example.birthdaylistoblopg.data.Person

data class PersonUIState(
    val isLoading: Boolean = false,
    val persons: List<Person> = emptyList(),
    val error: String? = null
)