package com.greenfodor.medical_reports_server.model

enum class UserRoles(val value: String) {
    ADMIN(value = "admin"),
    MEDICAL_LABORATORY_PROFESSIONAL(value = "mlp"),
    PHYSICIAN(value = "physician"),
    NURSE(value = "nurse")
}