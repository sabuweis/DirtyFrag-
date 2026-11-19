package com.android.bbkcalculator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowCompat
import com.android.bbkcalculator.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private var passwordInput = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupStealthSwitch()
        setupPasswordField()
        loadCurrentSettings()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "设置"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupStealthSwitch() {
        binding.switchStealth.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // 开启隐身模式前检查是否已设置密码
                val password = StealthManager.getStealthPassword(this)
                if (password == null || password.isEmpty()) {
                    Toast.makeText(this, "请先设置隐身密码", Toast.LENGTH_SHORT).show()
                    binding.switchStealth.isChecked = false
                    binding.etStealthPassword.requestFocus()
                    return@setOnCheckedChangeListener
                }
            }
            StealthManager.setStealthEnabled(this, isChecked)
            updatePasswordFieldVisibility(isChecked)
            Toast.makeText(this, 
                if (isChecked) "隐身模式已开启，下次启动将显示计算器界面" 
                else "隐身模式已关闭", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupPasswordField() {
        binding.etStealthPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                passwordInput = s.toString()
            }
        })
        
        binding.btnSavePassword.setOnClickListener {
            if (passwordInput.isEmpty()) {
                Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (passwordInput.length < 4) {
                Toast.makeText(this, "密码至少4位数字", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            StealthManager.setStealthPassword(this, passwordInput)
            Toast.makeText(this, "隐身密码已保存", Toast.LENGTH_SHORT).show()
            binding.etStealthPassword.text.clear()
            passwordInput = ""
        }
    }

    private fun loadCurrentSettings() {
        val isStealth = StealthManager.isStealthEnabled(this)
        binding.switchStealth.isChecked = isStealth
        updatePasswordFieldVisibility(isStealth)
        
        val currentPassword = StealthManager.getStealthPassword(this)
        if (currentPassword != null) {
            binding.tvPasswordHint.text = "当前密码: ${"*".repeat(currentPassword.length)}"
        }
    }

    private fun updatePasswordFieldVisibility(visible: Boolean) {
        binding.layoutPassword.visibility = if (visible) View.VISIBLE else View.GONE
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, SettingsActivity::class.java))
        }
    }
}