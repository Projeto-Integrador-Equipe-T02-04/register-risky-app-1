package com.example.register_risky_app1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterUserActivity : AppCompatActivity() {
    private lateinit var pucLogo: ImageView
    private lateinit var usernameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var senhaInput: EditText
    private lateinit var registerUser: Button
    private lateinit var returnLogin: Button

    private val auth = FirebaseAuth.getInstance()

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

    private fun createUser(email: String, senha: String) {
        auth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Usuário criado: ${user?.email}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Erro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

}