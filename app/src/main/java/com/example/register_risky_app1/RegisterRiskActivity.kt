package com.example.register_risky_app1

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterRiskActivity : AppCompatActivity() {
    private lateinit var pucLogo: ImageView
    private lateinit var riskDescription: EditText
    private lateinit var takePhotoButton: Button
    private lateinit var registerRiskButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_principal)

        pucLogo = findViewById(R.id.pucLogo)
        riskDescription = findViewById(R.id.riskDescription)
        takePhotoButton = findViewById(R.id.takePhotoButton)
        registerRiskButton = findViewById(R.id.registerRiskButton)

        takePhotoButton.setOnClickListener {
            Toast.makeText(this, "Foto e localização capturadas", Toast.LENGTH_SHORT).show()
        }

        registerRiskButton.setOnClickListener {
            validateInput()
        }
    }

    private fun validateInput() {
        val description = riskDescription.text.toString().trim()

        if (TextUtils.isEmpty(description)) {
            riskDescription.error = "A descrição do risco não pode estar vazia"
            return
        }

        riskDescription.error = null
        Toast.makeText(this, "Risco registrado com sucesso!", Toast.LENGTH_SHORT).show()
    }
}
