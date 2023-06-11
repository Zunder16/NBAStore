package com.guillermohernandezpomares.storenba.ui.carrito

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.guillermohernandezpomares.storenba.databinding.ActivityCarritoBinding
import com.guillermohernandezpomares.storenba.model.ProductoCarrito
import com.guillermohernandezpomares.storenba.model.Usuario
import com.guillermohernandezpomares.storenba.ui.pagar.PagarActivity
import com.guillermohernandezpomares.storenba.utils.CarritoRepo
import com.guillermohernandezpomares.storenba.utils.Constantes
import com.guillermohernandezpomares.storenba.utils.UsuarioRepo
import com.guillermohernandezpomares.storenba.utils.Utiles

class CarritoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCarritoBinding
    private val adapter: CarritoAdapter = CarritoAdapter()
    private val carritoRepo: CarritoRepo = CarritoRepo()
    private var usuario = Usuario()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarritoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usuario = Constantes.DATOS_USUARIO!!
        // Recoger por intent el usuario
        setAdapterCarrito()
        observeData()

        binding.btnPagar.setOnClickListener {
            val intent = Intent(this, PagarActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return Utiles.onCreateOptionsMenu(menuInflater, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return Utiles.onOptionsItemSelected(this, item)
    }

    fun refresh() {
        setAdapterCarrito()
        observeData()
    }

    private fun setAdapterCarrito() {
        binding.rvCarrito.setHasFixedSize(true)
        binding.rvCarrito.layoutManager = LinearLayoutManager(this)
        binding.rvCarrito.adapter = adapter
    }


    private fun observeData() {
        carritoRepo.getDatosCarrito(usuario.id).observe(this) {
            adapter.setCarrito(it, this)
            adapter.notifyDataSetChanged()
        }
    }
}