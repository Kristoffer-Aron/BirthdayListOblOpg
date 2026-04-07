package com.example.birthdaylistoblopg.data

interface PersonRepository {
    suspend fun getPersons(): NetworkResult<List<Person>>

}