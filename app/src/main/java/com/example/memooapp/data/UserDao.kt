package com.example.memooapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE username = :user LIMIT 1")
    suspend fun findByUsername(user: String): User?

    @Insert
    suspend fun insertUser(user: User)
}
