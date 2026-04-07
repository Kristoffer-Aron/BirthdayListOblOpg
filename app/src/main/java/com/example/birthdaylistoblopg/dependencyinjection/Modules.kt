package com.example.birthdaylistoblopg.dependencyinjection

import com.example.birthdaylistoblopg.data.PersonAPI
import com.example.birthdaylistoblopg.data.PersonRepository
import com.example.birthdaylistoblopg.data.PersonRepositoryImpl
import com.example.birthdaylistoblopg.viewmodel.PersonViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModules = module {
    single<PersonRepository> { PersonRepositoryImpl(get(), get()) }
    single { Dispatchers.IO }
    single { PersonViewModel(get()) }
    single {
        Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .baseUrl("https://birthdaysrest.azurewebsites.net/api/")
            .build()
    }
    single { get<Retrofit>().create(PersonAPI::class.java) }
}