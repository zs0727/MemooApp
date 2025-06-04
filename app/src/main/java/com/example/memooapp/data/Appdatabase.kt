package com.example.memooapp.data
import android.content.Context
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase



@Database(
    entities = [Memo::class,User::class],
    version = 2,
    exportSchema = false  // 开发阶段可禁用
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memoDao(): MemoDao
    abstract fun userDao():UserDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                // 添加详细错误处理
                try {
                    Room.databaseBuilder(
                        context.applicationContext,  // 必须用 ApplicationContext
                        AppDatabase::class.java,
                        "memo_database.db"  // 显式添加.db后缀
                    )
                        .fallbackToDestructiveMigration()  // 开发时允许破坏性迁移
                        .build()
                        .also { INSTANCE = it }
                } catch (e: Exception) {
                    throw IllegalStateException("Failed to initialize database", e)
                }
            }
        }
    }
}