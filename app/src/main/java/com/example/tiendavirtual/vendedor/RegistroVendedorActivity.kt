package com.example.tiendavirtual.vendedor

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tiendavirtual.Constantes
import com.example.tiendavirtual.R
import com.example.tiendavirtual.databinding.ActivityRegistroVendedorBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistroVendedorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroVendedorBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroVendedorBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnRegistrarV.setOnClickListener {
            validarInformacion()


        }

    }

    private var nombres = ""
    private var email = ""
    private var password = ""
    private var cpass = ""

    private fun validarInformacion() {
        email = binding.etEmail.text.toString().trim()
        password = binding.etPassword.text.toString().trim()
        nombres = binding.etNombres.text.toString().trim()
        cpass = binding.etCPassword.text.toString().trim()

        if (nombres.isEmpty()) {
            binding.etNombres.error = "Ingrese su nombre"
            binding.etNombres.requestFocus()
        }else if(email.isEmpty()){
            binding.etEmail.error = "Ingrese su email"
            binding.etEmail.requestFocus()
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(
                email
            ).matches()
        ) {
            binding.etEmail.error = "Email no valido"
            binding.etEmail.requestFocus()
        } else if (password.isEmpty()) {
            binding.etPassword.error = "Ingrese su contraseña"
            binding.etPassword.requestFocus()
        }else if(password.length <= 6) {
            binding.etPassword.error = "La contraseña debe tener al menos 6 caracteres"
            binding.etPassword.requestFocus()

        }
        else if (cpass.isEmpty()) {
            binding.etCPassword.error = "Confirme su contraseña"
            binding.etCPassword.requestFocus()
        } else if (password != cpass) {
            binding.etCPassword.error = "Las contraseñas no coinciden"
            binding.etCPassword.requestFocus()
        }
        else {
            registrarUsuario()
        }
    }

    private fun registrarUsuario() {
        progressDialog.setMessage("Creando usuario")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email,password).
        addOnSuccessListener {
            insertarInfoBd()
        }
            .addOnFailureListener { e->
                Toast.makeText(this, "Error al crear el usuario ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun insertarInfoBd() {
        progressDialog.setMessage("Guardando información")

        val uidBD = firebaseAuth.uid
        val nombresBD = nombres
        val emailBD = email
        val tipoUsuarioBD = "Vendedor"
        val tiempoBD = Constantes().obtenerTiempo()

        val datosVendedor = HashMap<String, Any>()
        datosVendedor["uid"] = "$uidBD"
        datosVendedor["nombres"] = "$nombresBD"
        datosVendedor["email"] = "$emailBD"
        datosVendedor["tipoUsuario"] = "$tipoUsuarioBD"
        datosVendedor["tiempo_resgitro"] = "$tiempoBD"

        val references = FirebaseDatabase.getInstance().getReference("Usuarios")
        references.child("$uidBD")
            .setValue(datosVendedor)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivityVendedor::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Error al guardar la información ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }
}