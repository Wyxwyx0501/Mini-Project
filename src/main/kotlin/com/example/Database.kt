package com.example

import com.example.models.Registration
import com.example.models.Activity
import java.sql.Connection
import java.sql.DriverManager

object Database {
    private const val DATABASE_URL = "JDBC:sqlite:club.db"

    private fun getConnection(): Connection {
        return DriverManager.getConnection(System.getProperty("club.database.url", DATABASE_URL))
    }

    fun init() {
        getConnection().use { connection ->
            connection.createStatement().use { statement ->
                statement.executeUpdate(
                    """
                    CREATE TABLE IF NOT EXISTS activities (
                        id TEXT PRIMARY KEY NOT NULL,
                        title TEXT NOT NULL,
                        location TEXT NOT NULL,
                        capacity INTEGER NOT NULL CHECK (capacity >= 0),
                        deadline TEXT NOT NULL,
                        status TEXT NOT NULL DEFAULT 'PENDING',
                        organizer_id TEXT NOT NULL,
                        club_name TEXT NOT NULL
                    )
                    """.trimIndent()
                )

                statement.executeUpdate(
                    """
                    CREATE TABLE IF NOT EXISTS registrations (
                        id TEXT PRIMARY KEY NOT NULL,
                        activity_id TEXT NOT NULL,
                        student_id TEXT NOT NULL,
                        student_name TEXT NOT NULL,
                        register_time TEXT NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }
    }
    fun saveActivity(activity: Activity) {
        val sql = """
        INSERT INTO activities (
            id, title, location, capacity, deadline,
            status, organizer_id, club_name
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """.trimIndent()

        getConnection().use { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, activity.id)
                statement.setString(2, activity.title)
                statement.setString(3, activity.location)
                statement.setInt(4, activity.capacity)
                statement.setString(5, activity.deadline)
                statement.setString(6, activity.status)
                statement.setString(7, activity.organizerId)
                statement.setString(8, activity.clubName)
                statement.executeUpdate()
            }
        }
    }

    fun getAllActivities(): List<Activity> {
        val activities = mutableListOf<Activity>()

        getConnection().use { connection ->
            connection.prepareStatement("SELECT * FROM activities ORDER BY id").use { statement ->
                statement.executeQuery().use { result ->
                    while (result.next()) {
                        activities.add(
                            Activity(
                                id = result.getString("id"),
                                title = result.getString("title"),
                                location = result.getString("location"),
                                capacity = result.getInt("capacity"),
                                deadline = result.getString("deadline"),
                                status = result.getString("status"),
                                organizerId = result.getString("organizer_id"),
                                clubName = result.getString("club_name")
                            )
                        )
                    }
                }
            }
        }

        return activities
    }
    fun updateActivityStatus(id: String, newStatus: String) {
        val sql = "UPDATE activities SET status = ? WHERE id = ?"

        getConnection().use { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, newStatus)
                statement.setString(2, id)

                val updatedRows = statement.executeUpdate()
                check(updatedRows == 1) { "Activity not found: $id" }
            }
        }
    }
    fun saveRegistration(reg: Registration) {
        val sql = """
        INSERT INTO registrations (
            id, activity_id, student_id, student_name, register_time
        ) VALUES (?, ?, ?, ?, ?)
    """.trimIndent()

        getConnection().use { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, reg.id)
                statement.setString(2, reg.activityId)
                statement.setString(3, reg.studentId)
                statement.setString(4, reg.studentName)
                statement.setString(5, reg.registerTime)
                statement.executeUpdate()
            }
        }
    }

    fun getRegistrationsByActivityId(activityId: String): List<Registration> {
        val registrations = mutableListOf<Registration>()
        val sql = """
        SELECT id, activity_id, student_id, student_name, register_time
        FROM registrations
        WHERE activity_id = ?
        ORDER BY register_time
    """.trimIndent()

        getConnection().use { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, activityId)

                statement.executeQuery().use { result ->
                    while (result.next()) {
                        registrations.add(
                            Registration(
                                id = result.getString("id"),
                                activityId = result.getString("activity_id"),
                                studentId = result.getString("student_id"),
                                studentName = result.getString("student_name"),
                                registerTime = result.getString("register_time")
                            )
                        )
                    }
                }
            }
        }

        return registrations
    }
}