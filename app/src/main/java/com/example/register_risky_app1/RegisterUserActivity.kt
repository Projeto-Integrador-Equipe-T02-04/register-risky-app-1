package com.example.register_risky_app1

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterUserActivity : AppCompatActivity() {
    private lateinit var pucLogo: ImageView
    private lateinit var usernameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var senhaInput: EditText
    private lateinit var registerUser: Button
    private lateinit var returnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        pucLogo = findViewById(R.id.pucLogo)
        usernameInput = findViewById(R.id.usernameInput)
        emailInput = findViewById(R.id.emailInput)
        senhaInput = findViewById(R.id.passwordInput)
        registerUser = findViewById(R.id.cadButton)
        returnLogin = findViewById(R.id.loginButton)

    }
}