package com.example

import com.example.database.ActivityRepository
import com.example.models.Activity
import io.ktor.server.application.*
import io.ktor.server.request.* // 必须加这个，用来接收数据
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.UUID

fun Application.configureRouting(){

    routing {
        get("/") {
            call.respondText("Server is running!")
        }

        // 1. 获取列表
        get("/api/activities") {
            val publishedActivities = ActivityRepository.getAllActivities().filter{it.status == "PUBLISHED"}
            call.respond(publishedActivities)
        }

        // 2. 发布活动（这就是你漏掉的 POST 接口！）
        post("/api/activities") {
            val newActivity = call.receive<Activity>() // 接收前端发来的 JSON
            val activityToSave = newActivity.copy(
                id = UUID.randomUUID().toString(), // 自动生成随机 ID
                status = "PENDING" // 强制设为待审核
            )
            ActivityRepository.saveActivity(activityToSave)
            call.respond(io.ktor.http.HttpStatusCode.Created, mapOf("message" to "Activity created successfully"))
        }
    }
}