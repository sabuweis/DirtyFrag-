# Keep native method names — exp.c binds to them via JNI.
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep the app package: exp.c does FindClass("com/android/bbkcalculator/...") and
# view binding generates com.android.bbkcalculator.databinding.* referenced reflectively.
-keep class com.android.bbkcalculator.** { *; }
-keep class com.android.bbkcalculator.databinding.** { *; }

# Keep click-handler wiring used by Material components.
-keepclassmembers class * {
    void *Click(...);
}

-dontwarn com.android.bbkcalculator.**