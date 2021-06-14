package com.greenfodor.medical_reports_server.db.dao

import com.google.gson.annotations.SerializedName
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.*

object MedicalReports : Table("medical_reports") {
    val reportNo: Column<Int> = integer("report_no")
    val patientId: Column<UUID> = uuid("patient_id")
    val authorId: Column<UUID> = uuid("author_id")
    val generalCondition: Column<Int?> = integer("general_condition").nullable()
    val heartAction: Column<Int?> = integer("heart_action").nullable()
    val heartSound: Column<Int?> = integer("heart_sound").nullable()
    val breathing: Column<Int?> = integer("breathing").nullable()
    val headInjury: Column<Int?> = integer("head_injury").nullable()
}

data class Report(
    val reportNo: Int,
    val patientId: String,
    val authorId: String,
    val generalCondition: Int?,
    val heartAction: Int?,
    val heartSound: Int?,
    val breathing: Int?,
    val headInjury: Int?
) {
    fun toGetReportResponse(patientName: String, authorName: String) = GetReportResponse(
        reportNo, patientName, authorName, generalCondition, heartAction, heartSound, breathing, headInjury
    )

    fun toGetReportsItem(authorName: String) = GetReportsItem(reportNo, patientId, authorName)
}

data class NewReport(
    @SerializedName("generalCondition")
    val generalCondition: Int?,
    @SerializedName("heartAction")
    val heartAction: Int?,
    @SerializedName("heartSound")
    val heartSound: Int?,
    @SerializedName("breathing")
    val breathing: Int?,
    @SerializedName("headInjury")
    val headInjury: Int?
) {
    fun toReport(reportNo: Int, patientId: String, authorId: String) =
        Report(reportNo, patientId, authorId, generalCondition, heartAction, heartSound, breathing, headInjury)
}

data class GetReportsItem(
    @SerializedName("reportNo")
    val reportNo: Int,
    @SerializedName("patientId")
    val patientId: String,
    @SerializedName("authorName")
    val authorName: String
)

data class GetReportResponse(
    @SerializedName("reportNo")
    val reportNo: Int,
    @SerializedName("patientName")
    val patientName: String,
    @SerializedName("authorName")
    val authorName: String,
    @SerializedName("generalCondition")
    val generalCondition: Int?,
    @SerializedName("heartAction")
    val heartAction: Int?,
    @SerializedName("heartSound")
    val heartSound: Int?,
    @SerializedName("breathing")
    val breathing: Int?,
    @SerializedName("headInjury")
    val headInjury: Int?
)