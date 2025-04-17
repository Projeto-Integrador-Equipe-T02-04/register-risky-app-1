package com.example.register_risky_app1

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_login)

        emailInputLayout = findViewById(R.id.emailInputLayout)
        passwordInputLayout = findViewById(R.id.passwordInputLayout)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            validateInput()
        }
    }

    private fun validateInput() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        if (TextUtils.isEmpty(email)) {
            emailInputLayout.error = "Email não pode ser vazio"
            return
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout.error = "Email inválido"
            return
        } else {
            emailInputLayout.error = null
        }

        if (TextUtils.isEmpty(password)) {
            passwordInputLayout.error = "Senha não pode ser vazia"
            return
        } else if (password.length < 6) {
            passwordInputLayout.error = "Senha deve ter pelo menos 6 caracteres"
            return
        } else {
            passwordInputLayout.error = null
        }

        Toast.makeText(this, "Login bem-sucedido", Toast.LENGTH_SHORT).show()

    }
}
