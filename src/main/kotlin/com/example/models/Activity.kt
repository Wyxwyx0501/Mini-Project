package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Activity(
    val id: String,
    val title: String,
    val location: String,
    val capacity: Int,
    val deadline: String,
    val status: String = "PENDING",

    val organizerId: String,   // 谁发的（学号，比如 "2021001"）
    val clubName: String       // 代表哪个社团发的（比如 "吉他社"）
)