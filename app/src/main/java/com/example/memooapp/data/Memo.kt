package com.example.memooapp.data
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memos")
data class Memo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
