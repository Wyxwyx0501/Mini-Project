package com.example

import com.example.models.Activity
import io.ktor.server.application.*
import io.ktor.server.request.* // 必须加这个，用来接收数据
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.UUID

fun Application.configureRouting() {
    // 把假数据放到外面，作为一个全局的临时“内存数据库”
    val fakeDatabase = mutableListOf(
        Activity("1", "吉他社迎新晚会", "一教101", 100, "2026-12-01", "PUBLISHED", "guitar_admin", "吉他社"),
        Activity("2", "ACM 算法培训", "机房302", 50, "2026-11-20", "PENDING", "acm_admin", "ACM协会")
    )

    routing {
        get("/") {
            call.respondText("Server is running!")
        }

        // 1. 获取列表
        get("/api/activities") {
            call.respond(fakeDatabase)
        }

        // 2. 发布活动（这就是你漏掉的 POST 接口！）
        post("/api/activities") {
            val newActivity = call.receive<Activity>() // 接收前端发来的 JSON
            val activityToSave = newActivity.copy(
                id = UUID.randomUUID().toString(), // 自动生成随机 ID
                status = "PENDING" // 强制设为待审核
            )
            fakeDatabase.add(activityToSave) // 存入内存
            call.respond(io.ktor.http.HttpStatusCode.Created, mapOf("message" to "Activity created successfully"))
        }
    }
}