package com.example.register_risky_app1

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_main_login)

        emailInputLayout = findViewById(R.id.emailInputLayout)
        passwordInputLayout = findViewById(R.id.passwordInputLayout)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.cadButton)

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

        login(email, password)
    }

    private fun login(email: String, senha: String) {
        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Logado como: ${user?.email}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Erro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

}
