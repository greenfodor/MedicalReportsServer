package com.greenfodor.medical_reports_server.db.dao

import com.google.gson.annotations.SerializedName
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime

object Patients : UUIDTable("patients") {
    val name: Column<String> = varchar("name", 100)
    val dob: Column<DateTime> = datetime("dob")
    val gender: Column<String> = varchar("gender", 100)
}

data class Patient(
    val id: String,
    val name: String,
    val dob: DateTime,
    val gender: String
) {
    fun toGetPatientResponse() = GetPatientResponse(id, name, dob.toString(), gender)
}

data class NewPatient(
    @SerializedName("name")
    val name: String,
    @SerializedName("dob")
    val dob: String,
    @SerializedName("gender")
    val gender: String
)

data class RegisterPatientResponse(
    @SerializedName("patientId")
    val patientId: String
)

data class GetPatientResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("dob")
    val dob: String,
    @SerializedName("gender")
    val gender: String
)