package com.example.birthdaylistoblopg.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PersonAPI {
    @GET("Persons")
    suspend fun getPersons(): Response<List<Person>>

    @GET("Persons")
    suspend fun getPersons(@Query("user_id") userId: String): Response<List<Person>>

    @POST("Persons")
    suspend fun addPerson(@Body person: Person): Response<Person>

}