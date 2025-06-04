package com.example.memooapp.data
import androidx.room.*
import com.example.memooapp.data.Memo

@Dao
interface MemoDao {

    @Query("SELECT * FROM memos WHERE userId = :userId ORDER BY timestamp DESC")
    suspend fun getMemosForUser(userId: String): List<Memo>
    //suspend fun getAllMemos(): List<Memo>

    @Insert
    suspend fun insertMemo(memo: Memo)

    @Update
    suspend fun updateMemo(memo: Memo)

    @Delete
    suspend fun deleteMemo(memo: Memo)
}
