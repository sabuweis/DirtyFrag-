package com.android.bbkcalculator.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// vivo计算器风格颜色
object CalculatorColors {
    // 深色模式（计算器默认深色）
    val Dark = CalculatorColorScheme(
        background = Color(0xFF000000),
        surface = Color(0xFF1C1C1C),
        surfaceContainer = Color(0xFF2A2A2A),
        surfaceContainerHigh = Color(0xFF333333),
        onBackground = Color(0xFFFFFFFF),
        onSurface = Color(0xFFFFFFFF),
        onSurfaceVariant = Color(0xFFB0B0B0),
        primary = Color(0xFF00C853),  // vivo绿
        onPrimary = Color(0xFF000000),
        primaryContainer = Color(0xFF004D1A),
        onPrimaryContainer = Color(0xFFFFFFFF),
        secondary = Color(0xFF6E6E6E),
        onSecondary = Color(0xFFFFFFFF),
        outline = Color(0xFF3A3A3A),
        outlineVariant = Color(0xFF2E2E2E),
        inverseSurface = Color(0xFFE0E0E0),
        inverseOnSurface = Color(0xFF1C1C1C),
        shadow = Color(0xFF000000),
        scrim = Color(0xFF000000),
        surfaceTint = Color(0xFF00C853),
        error = Color(0xFFCF6679),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFFFFF),
    )

    // 浅色模式
    val Light = CalculatorColorScheme(
        background = Color(0xFFF5F5F5),
        surface = Color(0xFFFFFFFF),
        surfaceContainer = Color(0xFFF0F0F0),
        surfaceContainerHigh = Color(0xFFE8E8E8),
        onBackground = Color(0xFF1C1C1C),
        onSurface = Color(0xFF1C1C1C),
        onSurfaceVariant = Color(0xFF6E6E6E),
        primary = Color(0xFF00B040),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFD0F0D8),
        onPrimaryContainer = Color(0xFF002008),
        secondary = Color(0xFF6E6E6E),
        onSecondary = Color(0xFFFFFFFF),
        outline = Color(0xFFC0C0C0),
        outlineVariant = Color(0xFFE0E0E0),
        inverseSurface = Color(0xFF1C1C1C),
        inverseOnSurface = Color(0xFFFFFFFF),
        shadow = Color(0xFF000000),
        scrim = Color(0xFF000000),
        surfaceTint = Color(0xFF00B040),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF410002),
    )
}

data class CalculatorColorScheme(
    val background: Color,
    val surface: Color,
    val surfaceContainer: Color,
    val surfaceContainerHigh: Color,
    val onBackground: Color,
    val onSurface: Color,
    val onSurfaceVariant: Color,
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val secondary: Color,
    val onSecondary: Color,
    val outline: Color,
    val outlineVariant: Color,
    val inverseSurface: Color,
    val inverseOnSurface: Color,
    val shadow: Color,
    val scrim: Color,
    val surfaceTint: Color,
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
)

val LocalCalculatorColors = staticCompositionLocalOf { CalculatorColors.Dark }