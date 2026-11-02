package com.example.database

import com.example.models.Activity

object ActivityRepository {

    fun saveActivity(activity: Activity) {
        val sql = """
            INSERT INTO activities (
                id, title, location, capacity, deadline,
                status, organizer_id, club_name
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """.trimIndent()

        DatabaseFactory.getConnection().use { connection ->
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

        DatabaseFactory.getConnection().use { connection ->
            connection.prepareStatement(
                "SELECT * FROM activities ORDER BY id"
            ).use { statement ->
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
}