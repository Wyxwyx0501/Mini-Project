package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Registration(
    val id: String,           // 报名记录自己的 ID
    val activityId: String,   // 关联的活动 ID
    val studentId: String,    // 学号
    val studentName: String,  // 姓名
    val registerTime: String  // 报名时间
)