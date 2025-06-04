// Compose 核心
package com.example.memooapp.ui.screens
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.memooapp.data.*
// Compose UI 组件
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import kotlinx.coroutines.withContext
// 布局和样式
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Android 平台
import android.content.Context
import android.widget.Toast
import androidx.compose.material3.TextButton
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLoginSuccess: (String) -> Unit,
                onNavigateToRegister: () -> Unit) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
    val db = AppDatabase.getDatabase(context)
    val userDao = db.userDao()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "登录", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("用户名") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("密码") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = rememberMe,
                onCheckedChange = { rememberMe = it }
            )
            Text("记住我")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (username.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "请先输入用户名和密码", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                // 后台查询：是否有这个用户、密码是否匹配
                CoroutineScope(Dispatchers.IO).launch {
                    val user = userDao.findByUsername(username)
                    if (user == null) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "用户不存在", Toast.LENGTH_SHORT).show()
                        }
                    } else if (user.password != password) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // 登录成功
                        if (rememberMe) {
                            prefs.edit().apply {
                                putBoolean("isLoggedIn", true)
                                putString("currentUserId", username)
                                apply()
                            }
                        } else {
                            prefs.edit().apply {
                                putBoolean("isLoggedIn", false)
                                putString("currentUserId", "")
                                apply()
                            }
                        }
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show()
                            onLoginSuccess(username)
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("登录")
        }
        TextButton(
            onClick = { onNavigateToRegister() }
        ) {
            Text("还没有账号？点击注册")
        }
    }
}
