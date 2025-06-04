package com.example.memooapp
import com.example.memooapp.ui.screens.*
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext

import com.example.memooapp.ui.theme.MemooAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            MemooAppTheme{
                // 确保在可组合函数作用域内调用
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val prefs = LocalContext.current.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
    val isLoggedIn = prefs.getBoolean("isLoggedIn", false)
    val savedUser = prefs.getString("currentUserId", "") ?: ""
    val startDest = if (isLoggedIn && savedUser.isNotEmpty()) "memo_list" else "login"

    NavHost(navController, startDestination = startDest) {
        composable("login") {
            LoginScreen ({ userId ->
                // 登录成功后，userId 就是刚刚的用户名
                navController.navigate("memo_list/$userId") {
                    popUpTo("login") { inclusive = true }
                }
            },
            onNavigateToRegister = {
                navController.navigate("register")
            }
            )
        }
        composable("register") {
            RegisterScreen(onRegisterSuccess = {
                // 注册成功后返回登录页面
                navController.popBackStack("login", inclusive = false)
            })
        }
        composable("memo_list/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            MemoListScreen(
                currentUserId = userId,
                onLogout = {
                    // 退出时清空 SharedPreferences
                    prefs.edit().apply {
                        putBoolean("isLoggedIn", false)
                        putString("currentUserId", "")
                        apply()
                    }
                    navController.navigate("login") {
                        popUpTo("memo_list/$userId") { inclusive = true }
                    }
                }
            )
        }
    }
}
