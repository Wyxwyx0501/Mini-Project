package com.example.database

import java.sql.Connection
import java.sql.DriverManager

object DatabaseFactory {
    private const val DATABASE_URL = "JDBC:sqlite:club.db"

    fun getConnection(): Connection {
        return DriverManager.getConnection(DATABASE_URL)
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
            }
        }
    }
}