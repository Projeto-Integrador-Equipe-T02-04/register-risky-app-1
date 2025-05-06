package com.example.register_risky_app1

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.view.View
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class RegisterRiskActivity : AppCompatActivity() {
    private lateinit var riskDescription: EditText
    private lateinit var takePhotoButton: Button
    private lateinit var registerRiskButton: Button
    private lateinit var imagePreview: ImageView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var imageUri: Uri? = null

    companion object {
        private const val REQUEST_IMAGE_PICK = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa_risco)

        loadingProgressBar = findViewById(R.id.loadingProgressBar)
        riskDescription = findViewById(R.id.descriptionEditText)
        takePhotoButton = findViewById(R.id.uploadImageButton)
        registerRiskButton = findViewById(R.id.submitRiskButton)
        imagePreview = findViewById(R.id.imagePreview)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        takePhotoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }

        registerRiskButton.setOnClickListener {
            validateAndUpload()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            imagePreview.setImageURI(imageUri)
        }
    }

    private fun validateAndUpload() {
        val description = riskDescription.text.toString().trim()

        if (TextUtils.isEmpty(description)) {
            riskDescription.error = "A descrição do risco não pode estar vazia"
            return
        }

        if (imageUri == null) {
            Toast.makeText(this, "Por favor, selecione uma imagem", Toast.LENGTH_SHORT).show()
            return
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                uploadToFirebase(description, imageUri!!, location)
            } else {
                Toast.makeText(this, "Não foi possível obter a localização", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadToFirebase(description: String, imageUri: Uri, location: Location) {
        loadingProgressBar.visibility = View.VISIBLE
        registerRiskButton.isEnabled = false

        val storageRef = FirebaseStorage.getInstance().reference.child("risks/${UUID.randomUUID()}.jpg")
        val uploadTask = storageRef.putFile(imageUri)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.printStackTrace()
                throw task.exception ?: Exception("Erro desconhecido no upload")
            }
            storageRef.downloadUrl
        }.addOnSuccessListener { uri ->
            val firestore = FirebaseFirestore.getInstance()
            val data = mapOf(
                "descricao" to description,
                "imagemUrl" to uri.toString(),
                "latitude" to location.latitude,
                "longitude" to location.longitude,
                "timestamp" to System.currentTimeMillis()
            )
            firestore.collection("risks").add(data)
                .addOnSuccessListener {
                    Toast.makeText(this, "Risco enviado com sucesso", Toast.LENGTH_SHORT).show()
                    riskDescription.setText("")
                    imagePreview.setImageResource(0)
                    this.imageUri = null
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    Toast.makeText(this, "Erro ao salvar no Firestore: ${e.message}", Toast.LENGTH_LONG).show()
                }
                .addOnCompleteListener {
                    loadingProgressBar.visibility = View.GONE
                    registerRiskButton.isEnabled = true
                }
        }.addOnFailureListener { e ->
            e.printStackTrace()
            Toast.makeText(this, "Erro no upload da imagem: ${e.message}", Toast.LENGTH_LONG).show()
            loadingProgressBar.visibility = View.GONE
            registerRiskButton.isEnabled = true
        }
    }
}
