package com.example.birthdaylistoblopg.data

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class PersonRepositoryImpl(
    private val personAPI: PersonAPI,
    private val dispatcher: CoroutineDispatcher
) : PersonRepository {
    override suspend fun getPersons(): NetworkResult<List<Person>> {
        return withContext(dispatcher) {
            try {
                val response = personAPI.getPersons()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        NetworkResult.Success(body)
                    } else {
                        NetworkResult.Error("Response body is null")
                    }
                } else {
                    NetworkResult.Error(response.message())
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                NetworkResult.Error(e.message ?: "Unknown error")
            }

        }
    }
}