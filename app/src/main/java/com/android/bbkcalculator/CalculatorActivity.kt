package com.android.bbkcalculator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.android.bbkcalculator.databinding.ActivityCalculatorBinding

class CalculatorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalculatorBinding
    private var currentInput = ""
    private var previousInput = ""
    private var currentOperator = ""
    private var isNewInput = true
    private var lastKeyTime = 0L
    private val keySequence = mutableListOf<Int>()
    private val correctPassword by lazy { StealthManager.getStealthPassword(this) ?: "1234" }
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 全屏沉浸式
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.insetsController?.systemBarsBehavior = 
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        
        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDisplay()
        setupNumberButtons()
        setupOperatorButtons()
        setupFunctionButtons()
        setupKeyListener()
    }

    private fun setupDisplay() {
        binding.tvDisplay.setText("0")
        binding.tvDisplay.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 检测密钥输入序列
                checkPasswordSequence(s.toString())
            }
        })
    }

    private fun setupNumberButtons() {
        val numberButtons = listOf(
            binding.btn0 to 0, binding.btn1 to 1, binding.btn2 to 2,
            binding.btn3 to 3, binding.btn4 to 4, binding.btn5 to 5,
            binding.btn6 to 6, binding.btn7 to 7, binding.btn8 to 8,
            binding.btn9 to 9
        )
        
        numberButtons.forEach { (btn, num) ->
            btn.setOnClickListener { onNumberClick(num) }
            btn.setOnLongClickListener { onNumberLongClick(num); true }
        }
    }

    private fun setupOperatorButtons() {
        binding.btnAdd.setOnClickListener { onOperatorClick("+") }
        binding.btnSubtract.setOnClickListener { onOperatorClick("-") }
        binding.btnMultiply.setOnClickListener { onOperatorClick("×") }
        binding.btnDivide.setOnClickListener { onOperatorClick("÷") }
        binding.btnEquals.setOnClickListener { onEqualsClick() }
    }

    private fun setupFunctionButtons() {
        binding.btnClear.setOnClickListener { onClearClick() }
        binding.btnClear.setOnLongClickListener { onClearLongClick(); true }
        binding.btnPlusMinus.setOnClickListener { onPlusMinusClick() }
        binding.btnPercent.setOnClickListener { onPercentClick() }
        binding.btnDecimal.setOnClickListener { onDecimalClick() }
        binding.btnBackspace.setOnClickListener { onBackspaceClick() }
        binding.btnBackspace.setOnLongClickListener { onClearClick(); true }
    }

    private fun onNumberClick(num: Int) {
        if (isNewInput) {
            currentInput = num.toString()
            isNewInput = false
        } else {
            if (currentInput.length < 12) {
                currentInput += num
            }
        }
        updateDisplay()
        checkPasswordFromButtons(num)
    }

    private fun onNumberLongClick(num: Int) {
        // 长按数字键可用于快捷操作
    }

    private fun onOperatorClick(op: String) {
        if (!currentInput.isEmpty() && !isNewInput) {
            if (!previousInput.isEmpty() && currentOperator.isNotEmpty()) {
                calculate()
            }
            previousInput = currentInput
            currentOperator = op
            isNewInput = true
            binding.tvOperator.text = op
        } else if (!previousInput.isEmpty()) {
            // 允许更换运算符
            currentOperator = op
            binding.tvOperator.text = op
        }
        checkOperatorFromButton(op)
    }

    private fun onEqualsClick() {
        if (!currentInput.isEmpty() && !previousInput.isEmpty() && currentOperator.isNotEmpty()) {
            calculate()
            currentOperator = ""
            binding.tvOperator.text = ""
        }
        checkEqualsFromButton()
    }

    private fun calculate() {
        val a = previousInput.toDoubleOrNull() ?: return
        val b = currentInput.toDoubleOrNull() ?: return
        var result: Double
        
        when (currentOperator) {
            "+" -> result = a + b
            "-" -> result = a - b
            "×" -> result = a * b
            "÷" -> {
                if (b == 0.0) {
                    binding.tvDisplay.text = "Error"
                    handler.postDelayed({ resetCalculator() }, 1500)
                    return
                }
                result = a / b
            }
            else -> return
        }
        
        // 格式化结果
        val resultStr = if (result == result.toLong()) result.toLong().toString() 
            else String.format("%.10f", result).replace(Regex("\\.?0+$"), "")
        
        currentInput = resultStr
        previousInput = ""
        isNewInput = true
        updateDisplay()
    }

    private fun onClearClick() {
        resetCalculator()
    }

    private fun onClearLongClick() {
        // 长按清除也重置
        resetCalculator()
    }

    private fun onPlusMinusClick() {
        if (currentInput.isNotEmpty() && currentInput != "0") {
            if (currentInput.startsWith("-")) {
                currentInput = currentInput.substring(1)
            } else {
                currentInput = "-$currentInput"
            }
            updateDisplay()
        }
    }

    private fun onPercentClick() {
        if (currentInput.isNotEmpty()) {
            val value = currentInput.toDoubleOrNull() ?: return
            currentInput = (value / 100).toString()
            updateDisplay()
        }
    }

    private fun onDecimalClick() {
        if (isNewInput) {
            currentInput = "0."
            isNewInput = false
        } else if (". " !in currentInput) {
            currentInput += "."
        }
        updateDisplay()
    }

    private fun onBackspaceClick() {
        if (!isNewInput && currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
            if (currentInput.isEmpty() || currentInput == "-") {
                currentInput = "0"
                isNewInput = true
            }
            updateDisplay()
        }
    }

    private fun resetCalculator() {
        currentInput = ""
        previousInput = ""
        currentOperator = ""
        isNewInput = true
        binding.tvOperator.text = ""
        binding.tvDisplay.text = "0"
    }

    private fun updateDisplay() {
        val displayText = if (currentInput.isEmpty()) "0" else currentInput
        binding.tvDisplay.text = formatNumber(displayText)
    }

    private fun formatNumber(input: String): String {
        // 简单格式化，添加千位分隔符
        val parts = input.split("\\.".toRegex())
        val integerPart = parts[0]
        val formattedInteger = integerPart.replace(Regex("\\B(?=(\\d{3})+(?!\\d))"), ",")
        return if (parts.size > 1) "$formattedInteger.${parts[1]}" else formattedInteger
    }

    private fun checkPasswordSequence(displayText: String) {
        // 检测密码输入：用户输入密码后点击+再点击=
        // 这里我们在display中检测特定模式
    }

    private fun checkPasswordFromButtons(num: Int) {
        // 从数字按钮点击记录密码序列
        val now = System.currentTimeMillis()
        if (now - lastKeyTime > 2000) {
            keySequence.clear()
        }
        lastKeyTime = now
        keySequence.add(num)
    }

    private fun checkOperatorFromButton(op: String) {
        val now = System.currentTimeMillis()
        if (now - lastKeyTime > 2000) {
            keySequence.clear()
        }
        lastKeyTime = now
        
        if (op == "+") {
            if (keySequence.joinToString("") == correctPassword) {
                keySequence.add(-1) // 标记+
            }
        }
    }

    private fun checkEqualsFromButton() {
        val now = System.currentTimeMillis()
        if (now - lastKeyTime > 2000) {
            keySequence.clear()
        }
        lastKeyTime = now
        
        if (keySequence.lastOrNull() == -1) {
            // 密码正确，+，= 序列完成
            unlockStealthMode()
        }
    }

    private fun setupKeyListener() {
        // 监听按键序列：密码 -> + -> =
        binding.root.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                val now = System.currentTimeMillis()
                if (now - lastKeyTime > 2000) {
                    keySequence.clear()
                }
                lastKeyTime = now
                
                when (keyCode) {
                    KeyEvent.KEYCODE_0 -> keySequence.add(0)
                    KeyEvent.KEYCODE_1 -> keySequence.add(1)
                    KeyEvent.KEYCODE_2 -> keySequence.add(2)
                    KeyEvent.KEYCODE_3 -> keySequence.add(3)
                    KeyEvent.KEYCODE_4 -> keySequence.add(4)
                    KeyEvent.KEYCODE_5 -> keySequence.add(5)
                    KeyEvent.KEYCODE_6 -> keySequence.add(6)
                    KeyEvent.KEYCODE_7 -> keySequence.add(7)
                    KeyEvent.KEYCODE_8 -> keySequence.add(8)
                    KeyEvent.KEYCODE_9 -> keySequence.add(9)
                    KeyEvent.KEYCODE_PLUS -> {
                        if (keySequence.joinToString("") == correctPassword) {
                            keySequence.add(-1) // 标记+
                        }
                    }
                    KeyEvent.KEYCODE_EQUALS, KeyEvent.KEYCODE_ENTER -> {
                        if (keySequence.lastOrNull() == -1) {
                            // 密码正确，+，= 序列完成
                            unlockStealthMode()
                            return@setOnKeyListener true
                        }
                    }
                }
            }
            false
        }
    }

    private fun unlockStealthMode() {
        // 跳转到MainActivity（Root UI）
        Toast.makeText(this, "隐身模式已解锁", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
        overridePendingTransition(0, 0)
    }

    override fun onBackPressed() {
        // 计算器模式下不退出，表现得像真计算器
        moveTaskToBack(true)
    }
}