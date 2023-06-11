package com.guillermohernandezpomares.storenba.ui.login

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.guillermohernandezpomares.storenba.databinding.ActivityLoginBinding
import com.guillermohernandezpomares.storenba.ui.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val autorizacion: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (autorizacion.currentUser != null) {
            navegarMain()
        }


        loguear()
        registrar()
    }

    private fun registrar() {
        binding.tvCrearCuenta.setOnClickListener {
            navegarRegistro()
        }
    }

    private fun loguear() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etUsuario.editText?.text.toString()
            val password = binding.etContrasenia.editText?.text.toString()

            if (comprobacion()) {
                autorizacion.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            navegarMain()
                        } else {
                            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show()
                            Log.e(TAG, "Error al logar el usuario", task.exception)
                        }
                    }
            } else {
                Toast.makeText(this, "Error al introducir datos", Toast.LENGTH_LONG).show()
            }


        }
    }


    private fun navegarRegistro() {
        val intent = Intent(this, RegistroActivity::class.java)
        startActivity(intent)
    }

    private fun navegarMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun comprobacion(): Boolean {
        return if (binding.etUsuario.editText?.text.toString()
                .isEmpty() || binding.etContrasenia.editText?.text.toString().isEmpty()
        ) {
            false
        } else validarCorreo(binding.etUsuario.editText?.text.toString())

    }

    private fun validarCorreo(correo: String): Boolean {

        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(correo).matches()

    }
}