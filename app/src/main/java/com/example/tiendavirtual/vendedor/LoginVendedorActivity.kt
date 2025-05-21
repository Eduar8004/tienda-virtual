package com.example.tiendavirtual.vendedor

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tiendavirtual.R
import com.example.tiendavirtual.databinding.ActivityLoginVendedorBinding
import com.google.firebase.auth.FirebaseAuth

class LoginVendedorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginVendedorBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginVendedorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere un momento")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnIngresarV.setOnClickListener {
            val email = binding.etEmailVendedor.text.toString().trim()
            val password = binding.etPasswordVendedor.text.toString().trim()

            if (email.isEmpty()) {
                binding.etEmailVendedor.error = "Ingrese su correo"
                binding.etEmailVendedor.requestFocus()
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etEmailVendedor.error = "Ingrese un correo valido"
                binding.etEmailVendedor.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.etPasswordVendedor.error = "Ingrese su contraseña"
                binding.etPasswordVendedor.requestFocus()
                return@setOnClickListener
            }

            loginUser(email, password)
        }

        binding.tvRegistroVendedor.setOnClickListener {
            startActivity(Intent(this, RegistroVendedorActivity::class.java))
        }

    }

    private fun loginUser(email: String, password: String) {
        progressDialog.setMessage("Iniciando sesión...")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                val intent = Intent(this, MainActivityVendedor::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                binding.etPasswordVendedor.error = "Error: ${e.message}"
            }

    }
}