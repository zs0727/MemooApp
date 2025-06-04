package com.example.memooapp.ui.screens
// Compose 基础
import android.app.Application
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*

// 布局和组件
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*

// 平台相关
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.memooapp.viewmodel.*

import androidx.compose.material.icons.filled.Logout
//@Composable
//fun MemoListScreen() {
//    // 假数据：真实数据我们之后会从数据库读取
//    val memoList = listOf(
//        "去超市买菜",
//        "周五提交项目报告",
//        "阅读 Jetpack Compose 文档",
//        "晚上八点看电影"
//    )
//
//    Column(modifier = Modifier
//        .fillMaxSize()
//        .padding(16.dp)) {
//
//        Text(
//            text = "我的备忘录",
//            fontSize = 24.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        LazyColumn {
//            items(memoList) { memo ->
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 8.dp),
//                    elevation = CardDefaults.cardElevation(4.dp)
//                ) {
//                    Text(
//                        text = memo,
//                        fontSize = 18.sp,
//                        modifier = Modifier.padding(16.dp)
//                    )
//                }
//            }
//        }
//    }
//}

class MemoViewModelFactory(
    private val application: Application,
    private val currentUserId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MemoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MemoViewModel(application, currentUserId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoListScreen(
    currentUserId: String,
    onLogout: () -> Unit  // 增加一个回调：点击退出后让外层 NavController 跳回登录页
) {
    var showAddDialog by remember { mutableStateOf(false) }


    // 从 ViewModel 拿到当前 memos 列表
    //val memoList by viewModel.memos.collectAsState()
    val context = LocalContext.current
    //val prefs = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
    //val currentUserId = prefs.getString("currentUserId", "") ?: ""
    val viewModel: MemoViewModel = viewModel(
        factory = MemoViewModelFactory(
            context.applicationContext as Application,
            currentUserId
        )
    )

    val memoList by viewModel.memos.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "我的备忘录", fontSize = 20.sp) },
                actions = {
                    IconButton(onClick = {
                        // 1. 清除 SharedPreferences 中的登录标记
                        context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
                            .edit().putBoolean("isLoggedIn", false).apply()
                        // 2. 调用外层传入的 onLogout()，由它去导航到登录页
                        onLogout()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Logout,

                            contentDescription = "退出登录",
                            tint = Color.Black
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // 弹出添加备忘录对话框或导航到编辑页
                    showAddDialog = true
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "添加")
            }
        }
    ) { paddingValues ->
        // 内容区：给 Column、LazyColumn 加上 paddingValues，让内容不要顶到状态栏／AppBar 底下
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)   // 关键：让内容下移到 AppBar 下面
                .padding(16.dp)
        ) {
            if (memoList.isEmpty()) {
                Text(
                    text = "暂无备忘录",
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 32.dp)
                )
            } else {
                LazyColumn {
                    items(memoList) { memo ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    // 后续可以跳转详情页或编辑页
                                },
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = memo.title,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = memo.content,
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    if (showAddDialog) {
        AddMemoDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, content ->
                viewModel.addMemo(title, content)
                showAddDialog = false
            }
        )
    }
}