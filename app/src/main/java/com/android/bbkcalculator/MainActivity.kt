package com.android.bbkcalculator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.WindowCompat
import com.android.bbkcalculator.ui.component.SimpleFloatingBottomBar
import com.android.bbkcalculator.ui.component.BottomBarItem
import com.android.bbkcalculator.ui.theme.CalculatorTheme
import com.android.bbkcalculator.ui.theme.LocalCalculatorColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home as FilledHome
import androidx.compose.material.icons.filled.Settings as FilledSettings
import androidx.compose.material.icons.rounded.Home as RoundedHome
import androidx.compose.material.icons.rounded.Settings as RoundedSettings

class MainActivity : AppCompatActivity() {
    private lateinit var composeView: ComposeView
    private var isRooted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        // 检查Root状态
        isRooted = checkRootStatus()
        
        composeView = ComposeView(this).apply {
            setViewCompositionStrategy(ComposeView.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                CalculatorTheme {
                    MainScreen(
                        isRooted = isRooted,
                        onSettingsClick = { SettingsActivity.start(this@MainActivity) },
                        onRunClick = { runExploit() },
                        onSuManagerClick = { showSuManagerPicker() }
                    )
                }
            }
        }
        setContentView(composeView)
    }

    private fun checkRootStatus(): Boolean {
        return java.io.File("/dev/df").exists()
    }

    private fun runExploit() {
        // TODO: 启动原有的ExploitRunner逻辑
        Toast.makeText(this, "Root功能待实现", Toast.LENGTH_SHORT).show()
    }

    private fun showSuManagerPicker() {
        // TODO: 显示SU Manager选择器
        Toast.makeText(this, "SU Manager选择待实现", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return false
    }

    @Composable
    fun MainScreen(
        isRooted: Boolean,
        onSettingsClick: () -> Unit,
        onRunClick: () -> Unit,
        onSuManagerClick: () -> Unit
    ) {
        val colors = LocalCalculatorColors.current
        val scope = rememberCoroutineScope()
        
        Surface(
            modifier = androidx.compose.foundation.layout.Modifier.fillMaxSize(),
            color = colors.background,
            content = {
                androidx.compose.foundation.layout.Box(
                    modifier = androidx.compose.foundation.layout.Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.BottomCenter
                ) {
                    // 主内容区域
                    androidx.compose.foundation.layout.Column(
                        modifier = androidx.compose.foundation.layout.Modifier
                            .fillMaxSize()
                            .padding(bottom = 100.dp), // 为底栏留空间
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        // Root状态指示器
                        if (isRooted) {
                            androidx.compose.material3.Text(
                                text = "Root 已获取 ✓",
                                color = colors.primary,
                                fontSize = 24.sp,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                            )
                            androidx.compose.foundation.layout.Spacer(modifier = androidx.compose.foundation.layout.Modifier.padding(16.dp))
                        }
                        
                        // 主功能按钮
                        androidx.compose.material3.Button(
                            onClick = onRunClick,
                            modifier = androidx.compose.foundation.layout.Modifier
                                .fillMaxWidth(0.8f)
                                .height(56.dp),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = colors.primary,
                                contentColor = colors.onPrimary
                            )
                        ) {
                            androidx.compose.material3.Text(
                                text = if (isRooted) "重新获取 Root" else "开始获取 Root",
                                fontSize = 18.sp,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                            )
                        }
                        
                        androidx.compose.foundation.layout.Spacer(modifier = androidx.compose.foundation.layout.Modifier.padding(16.dp))
                        
                        // SU Manager选择
                        androidx.compose.material3.OutlinedButton(
                            onClick = onSuManagerClick,
                            modifier = androidx.compose.foundation.layout.Modifier
                                .fillMaxWidth(0.8f)
                                .height(48.dp),
                            colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                                contentColor = colors.onSurface,
                                outlinedBorderColor = colors.outline
                            )
                        ) {
                            androidx.compose.material3.Text(
                                text = "选择 SU Manager",
                                fontSize = 16.sp
                            )
                        }
                    }
                    
                    // 液态玻璃底栏 - 仅主页和设置两项
                    SimpleFloatingBottomBar(
                        selectedIndex = 0,
                        onDestinationClick = { index ->
                            when (index) {
                                0 -> { /* 主页，已在首页 */ }
                                1 -> onSettingsClick() // 设置
                            }
                        },
                        items = listOf(
                            BottomBarItem(
                                label = "主页",
                                icon = RoundedHome,
                                selectedIcon = FilledHome
                            ),
                            BottomBarItem(
                                label = "设置",
                                icon = RoundedSettings,
                                selectedIcon = FilledSettings
                            )
                        )
                    )
                }
            }
        )
    }

    private fun rememberCoroutineScope(): CoroutineScope {
        return CoroutineScope(Dispatchers.Main)
    }
}