package com.greenfodor.medical_reports_server.db.dao

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class ReportsService {

    fun getAllPatientReports(patientId: String): List<Report> = transaction {
        MedicalReports.select { MedicalReports.patientId eq UUID.fromString(patientId) }.map { it.toReport() }
    }

    fun getNextReportNo(patientId: String): Int = transaction {
        val reports = MedicalReports.select {
            MedicalReports.patientId eq UUID.fromString(patientId)
        }.map { it.toReport() }

        reports.size + 1
    }

    fun getReport(reportNo: Int, patientId: String): Report? = transaction {
        MedicalReports.select {
            (MedicalReports.patientId eq UUID.fromString(patientId)) and (MedicalReports.reportNo eq reportNo)
        }
            .mapNotNull { it.toReport() }
            .singleOrNull()
    }

    fun createReport(report: Report) = transaction {
        MedicalReports.insert {
            it[reportNo] = report.reportNo
            it[patientId] = UUID.fromString(report.patientId)
            it[authorId] = UUID.fromString(report.authorId)
            it[generalCondition] = report.generalCondition
            it[heartAction] = report.heartAction
            it[heartSound] = report.heartSound
            it[breathing] = report.breathing
            it[headInjury] = report.headInjury
        }.resultedValues?.firstOrNull()?.toReport()
    }

    private fun ResultRow.toReport(): Report = Report(
        reportNo = this[MedicalReports.reportNo],
        patientId = this[MedicalReports.patientId].toString(),
        authorId = this[MedicalReports.authorId].toString(),
        generalCondition = this[MedicalReports.generalCondition],
        heartAction = this[MedicalReports.heartAction],
        heartSound = this[MedicalReports.heartSound],
        breathing = this[MedicalReports.breathing],
        headInjury = this[MedicalReports.headInjury]
    )
}