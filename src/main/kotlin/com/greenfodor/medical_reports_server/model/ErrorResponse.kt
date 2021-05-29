package com.greenfodor.medical_reports_server.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int? = null,
    @SerializedName("code") val code: String? = null
)