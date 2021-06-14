package com.greenfodor.medical_reports_server.db.dao

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

class PatientService {

    fun getPatientById(id: String): Patient? = transaction {
        Patients.select { Patients.id eq UUID.fromString(id) }
            .mapNotNull { it.toPatient() }
            .singleOrNull()
    }

    fun createPatient(newPatient: NewPatient) = transaction {
        Patients.insert {
            it[name] = newPatient.name
            it[dob] = DateTime.parse(newPatient.dob)
            it[gender] = newPatient.gender
        }.resultedValues?.firstOrNull()?.toPatient()
    }


    private fun ResultRow.toPatient(): Patient = Patient(
        id = this[Patients.id].toString(),
        name = this[Patients.name],
        dob = this[Patients.dob],
        gender = this[Patients.gender]
    )
}