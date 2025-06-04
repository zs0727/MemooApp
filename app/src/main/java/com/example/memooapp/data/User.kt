package com.example.memooapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val password: String  // 最简单示例：明文存。如果要更安全，可考虑加盐哈希
)