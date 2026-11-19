package com.android.bbkcalculator

import android.content.Context
import android.content.SharedPreferences

object StealthManager {
    private const val PREFS_NAME = "stealth_prefs"
    private const val KEY_STEALTH_ENABLED = "stealth_enabled"
    private const val KEY_STEALTH_PASSWORD = "stealth_password"
    private const val KEY_FIRST_LAUNCH = "first_launch"

    fun isStealthEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_STEALTH_ENABLED, false)
    }

    fun setStealthEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_STEALTH_ENABLED, enabled).apply()
    }

    fun getStealthPassword(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_STEALTH_PASSWORD, null)
    }

    fun setStealthPassword(context: Context, password: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_STEALTH_PASSWORD, password).apply()
    }

    fun isFirstLaunch(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_FIRST_LAUNCH, true)
    }

    fun setFirstLaunchComplete(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply()
    }

    fun verifyPassword(context: Context, input: String): Boolean {
        val saved = getStealthPassword(context)
        return saved != null && saved == input
    }
}