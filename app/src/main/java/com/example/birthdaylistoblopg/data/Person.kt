package com.example.birthdaylistoblopg.data

import com.google.gson.annotations.SerializedName

data class Person(
    @SerializedName("id")
    val id: Int = -1,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("birthYear")
    val birthYear: Int,
    @SerializedName("birthMonth")
    val birthMonth: Int,
    @SerializedName("birthDayOfMonth")
    val birthDayOfMonth: Int,
    @SerializedName("remarks")
    val remarks: String,
    @SerializedName("age")
    val age: Int,
) {
    val birthday: String
        get() = "$birthYear${birthMonth.toString().padStart(2, '0')}${birthDayOfMonth.toString().padStart(2, '0')}"
}