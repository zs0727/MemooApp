package com.example.memooapp.viewmodel
import com.example.memooapp.data.*
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.memooapp.data.Memo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch




class MemoViewModel(application: Application,
                    private val currentUserId: String ) : AndroidViewModel(application) {


    private val db = AppDatabase.getDatabase(application)
    private val dao = db.memoDao()

    private val _memos = MutableStateFlow<List<Memo>>(emptyList())
    val memos: StateFlow<List<Memo>> = _memos

    init {
        loadMemosForCurrentUser()
    }

    fun loadMemosForCurrentUser() {
        viewModelScope.launch {
            _memos.value = dao.getMemosForUser(currentUserId)
        }
    }

    fun addMemo(title: String, content: String) {
        viewModelScope.launch {
            val newMemo = Memo(
                userId = currentUserId,
                title = title,
                content = content
            )
            dao.insertMemo(newMemo)
            loadMemosForCurrentUser()
        }
    }

    fun deleteMemo(memo: Memo) {
        viewModelScope.launch {
            dao.deleteMemo(memo)
            loadMemosForCurrentUser()
        }
    }
}
