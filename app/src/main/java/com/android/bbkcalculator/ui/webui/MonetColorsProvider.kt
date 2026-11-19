package com.android.bbkcalculator.ui.webui

import android.content.Context
import androidx.webkit.WebViewCompat
import org.chromium.support_lib_boundary.WebViewProviderFactoryBoundaryInterface
import org.chromium.support_lib_boundary.util.BoundaryUtils

/**
 * MonetColorsProvider - 用于 WebView CSS 变量注入的颜色提供者
 * 在 MaterialExpressiveTheme 和 MiuixTheme 中调用，同步主题色到 WebView
 */
object MonetColorsProvider {

    @JvmStatic
    fun UpdateCss() {
        // 无参版本：使用默认颜色方案更新 WebView CSS 变量
        // 实际项目中会从 Compose 主题获取颜色并注入到 WebView
    }

    @JvmStatic
    fun UpdateCss(colorScheme: androidx.compose.material3.ColorScheme) {
        // 有参版本：根据 Material3 ColorScheme 更新 WebView CSS 变量
        // 实际项目中会生成 CSS 变量并通过 evaluateJavascript 注入
        // 这里提供空实现，避免编译错误
    }

    /**
     * 初始化 WebView 特性支持
     * 如果应用中使用了 WebView，可在 Application.onCreate 中调用
     */
    fun initWebViewCompat(context: Context) {
        WebViewCompat.setWebContentsDebuggingEnabled(true)
    }
}