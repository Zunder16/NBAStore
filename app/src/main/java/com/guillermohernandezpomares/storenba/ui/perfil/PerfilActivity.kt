package com.guillermohernandezpomares.storenba.ui.perfil

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.guillermohernandezpomares.storenba.R
import com.guillermohernandezpomares.storenba.databinding.ActivityPerfilBinding
import com.guillermohernandezpomares.storenba.model.Usuario
import com.guillermohernandezpomares.storenba.ui.MainActivity
import com.guillermohernandezpomares.storenba.ui.carrito.CarritoActivity
import com.guillermohernandezpomares.storenba.ui.login.LoginActivity
import com.guillermohernandezpomares.storenba.ui.perfil.datos.DatosActivity
import com.guillermohernandezpomares.storenba.ui.perfil.datos.DatosSeguridadActivity
import com.guillermohernandezpomares.storenba.ui.perfil.pedido.PedidoActivity
import com.guillermohernandezpomares.storenba.utils.Constantes
import com.guillermohernandezpomares.storenba.utils.Utiles

class PerfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPerfilBinding
    private val autorizacion: FirebaseAuth = FirebaseAuth.getInstance()
    private val baseDatos: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvDatos.setOnClickListener {
            val intent = Intent(this, DatosActivity::class.java)
            startActivity(intent)
        }

        binding.tvPedidos.setOnClickListener {
            val intent = Intent(this, PedidoActivity::class.java)
            startActivity(intent)
        }
        binding.tvSeguridad.setOnClickListener {
            val intent = Intent(this, DatosSeguridadActivity::class.java)
            startActivity(intent)
        }

        binding.ivCerrarSesion.setOnClickListener {
            dialogo()
        }

    }

    private fun dialogo() {
        val builder = AlertDialog.Builder(this)
        // Se crea el AlertDialog.
        builder.apply {
            // Se asigna un título.
            setTitle("Cerrar sesion")
            // Se asgina el cuerpo del mensaje.
            setMessage("¿Desea cerrar la sesión?")
            // Se define el comportamiento de los botones.
            setPositiveButton(android.R.string.ok) { _, _ ->
                cerrarSesion()
            }
            setNegativeButton(android.R.string.no) { _, _ ->
                Toast.makeText(
                    context,
                    android.R.string.no,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        // Se muestra el AlertDialog.
        builder.show()
    }


    private fun cerrarSesion() {
        autorizacion.signOut()
        Constantes.DATOS_USUARIO = Usuario()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return Utiles.onCreateOptionsMenu(menuInflater, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return Utiles.onOptionsItemSelected(this, item)
    }
}