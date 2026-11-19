package com.example

import com.example.models.Activity
import com.example.models.Registration
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DatabaseTest {
    @Test
    fun `activity status and registration are saved`() {
        val dbFile = Files.createTempFile("club-test-", ".db")
        System.setProperty("club.database.url", "jdbc:sqlite:${dbFile.toAbsolutePath()}")

        try {
            Database.init()

            val activity = Activity(
                "test-activity", "测试活动", "一教101", 30,
                "2026-12-01", "PENDING", "test-admin", "测试社团"
            )
            Database.saveActivity(activity)
            Database.updateActivityStatus(activity.id, "PUBLISHED")
            assertEquals("PUBLISHED", Database.getAllActivities().single().status)

            val registration = Registration(
                "test-registration", activity.id, "2021001",
                "测试同学", "2026-11-03"
            )
            Database.saveRegistration(registration)
            assertEquals(
                listOf(registration),
                Database.getRegistrationsByActivityId(activity.id)
            )
            assertTrue(Database.getRegistrationsByActivityId("other-activity").isEmpty())
        } finally {
            System.clearProperty("club.database.url")
            Files.deleteIfExists(dbFile)
        }
    }
}