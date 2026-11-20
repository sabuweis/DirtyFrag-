package com.android.bbkcalculator.data.repository

import android.content.Context
import android.content.SharedPreferences

class SettingsRepositoryImpl(context: Context? = null) : SettingsRepository {
    private val prefs: SharedPreferences? = context?.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
    
    // This is a minimal implementation for the theme system
    // In a full app, this would read from SharedPreferences
    
    override val themeMode: Int get() = prefs?.getInt("theme_mode", 2) ?: 2  // DARK
    override val miuixMonet: Boolean get() = prefs?.getBoolean("miuix_monet", false) ?: false
    override val keyColor: Int get() = prefs?.getInt("key_color", 0xFF00C853) ?: 0xFF00C853  // vivo green
    override val colorStyle: String get() = prefs?.getString("color_style", "TonalSpot") ?: "TonalSpot"
    override val colorSpec: String get() = prefs?.getString("color_spec", "SPEC_2025") ?: "SPEC_2025"
    override val uiMode: String get() = prefs?.getString("ui_mode", "miuix") ?: "miuix"
}

// Default instance for when no Context is available (e.g., in default parameters)
object DefaultSettingsRepository : SettingsRepository {
    override val themeMode: Int = 2  // DARK
    override val miuixMonet: Boolean = false
    override val keyColor: Int = 0xFF00C853  // vivo green
    override val colorStyle: String = "TonalSpot"
    override val colorSpec: String = "SPEC_2025"
    override val uiMode: String = "miuix"
}