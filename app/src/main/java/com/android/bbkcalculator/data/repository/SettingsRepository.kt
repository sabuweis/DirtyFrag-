package com.android.bbkcalculator.data.repository

interface SettingsRepository {
    val themeMode: Int
    val miuixMonet: Boolean
    val keyColor: Int
    val colorStyle: String
    val colorSpec: String
    val uiMode: String
}