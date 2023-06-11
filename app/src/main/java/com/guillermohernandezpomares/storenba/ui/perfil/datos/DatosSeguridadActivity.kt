package com.guillermohernandezpomares.storenba.ui.perfil.datos

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.guillermohernandezpomares.storenba.databinding.ActivityDatosSeguridadBinding
import com.guillermohernandezpomares.storenba.model.UsuarioInsertar
import com.guillermohernandezpomares.storenba.ui.MainActivity
import com.guillermohernandezpomares.storenba.utils.Constantes
import com.guillermohernandezpomares.storenba.utils.UsuarioRepo
import com.guillermohernandezpomares.storenba.utils.Utiles

class DatosSeguridadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDatosSeguridadBinding
    private val autorizacion: FirebaseAuth = FirebaseAuth.getInstance()
    private val usuarioRepo: UsuarioRepo = UsuarioRepo()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDatosSeguridadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setValoresPerfil()

        binding.btnModificarSeguridad.setOnClickListener {
            modificarCorreo()
        }
        binding.btnModificarContrasenia.setOnClickListener {
            modificarContrasenya()
        }

    }

    private fun modificarContrasenya() {

        // Comprobar que tienen formato de contraseña correcto y que son iguales

        val nuevaContrasenya = binding.etContraseniaNueva.editText!!.text.toString()

        if (validarContrasenya(nuevaContrasenya)){
            autorizacion.currentUser!!.updatePassword(nuevaContrasenya).addOnSuccessListener {
                // Algo para saber que hemos actualizado la contraseña
                Toast.makeText(this, "La contraseña se ha modificado", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        Toast.makeText(this, "La contraseña es incorrecto", Toast.LENGTH_LONG).show()


    }

    private fun validarContrasenya(password: String): Boolean {
        val patron = "^(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]+$".toRegex()
        return patron.matches(password)
    }

    private fun modificarCorreo() {

        val nuevoCorreo = binding.etCorreoModificar.editText!!.text.toString()
        if(validarCorreo(nuevoCorreo)){
            autorizacion.currentUser!!.updateEmail(nuevoCorreo).addOnSuccessListener {
                // Algo para saber que hemos actualizado el email
                Toast.makeText(this, "El correro se ha modificado", Toast.LENGTH_LONG).show()
                usuarioRepo.modifyUsuario(Constantes.DATOS_USUARIO!!.id, actualizacionDatosUsuario(nuevoCorreo))
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        Toast.makeText(this, "El correro es incorrecto", Toast.LENGTH_LONG).show()


    }

    private fun actualizacionDatosUsuario(nuevoCorreo: String): UsuarioInsertar {
        val usuarioInsertar = UsuarioInsertar()
        usuarioInsertar.nombre = Constantes.DATOS_USUARIO!!.nombre
        usuarioInsertar.apellidos = Constantes.DATOS_USUARIO!!.apellidos
        usuarioInsertar.correo = nuevoCorreo
        usuarioInsertar.fotoUsuario = Constantes.DATOS_USUARIO!!.fotoUsuario
        Constantes.DATOS_USUARIO!!.correo = nuevoCorreo
        return usuarioInsertar
    }

    private fun validarCorreo(correo: String): Boolean {

        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(correo).matches()

    }

    private fun setValoresPerfil() {
        val usuario = Constantes.DATOS_USUARIO
        if (usuario != null) {
            binding.etCorreoModificar.editText?.setText(usuario.correo)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return Utiles.onCreateOptionsMenu(menuInflater, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return Utiles.onOptionsItemSelected(this, item)
    }

}