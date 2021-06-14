package com.greenfodor.medical_reports_server.model

enum class UserRolesEnum(val value: String) {
    ADMIN(value = "admin"),
    MEDICAL_LABORATORY_PROFESSIONAL(value = "mlp"),
    PHYSICIAN(value = "physician"),
    NURSE(value = "nurse")
}