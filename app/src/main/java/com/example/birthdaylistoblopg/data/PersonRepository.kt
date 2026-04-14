package com.example.birthdaylistoblopg.data

interface PersonRepository {
    suspend fun getPersons(): NetworkResult<List<Person>>
    suspend fun getPersons(userId: String): NetworkResult<List<Person>>
    suspend fun addPerson(person: Person): NetworkResult<Person>
    suspend fun deletePerson(id: Int): NetworkResult<Person>
    suspend fun updatePerson(id: Int, person: Person): NetworkResult<Person>
}