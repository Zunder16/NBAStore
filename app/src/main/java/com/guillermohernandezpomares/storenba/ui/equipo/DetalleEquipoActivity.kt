package com.guillermohernandezpomares.storenba.ui.equipo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.guillermohernandezpomares.storenba.R
import com.guillermohernandezpomares.storenba.databinding.ActivityDetalleEquipoBinding
import com.guillermohernandezpomares.storenba.model.Equipo
import com.guillermohernandezpomares.storenba.ui.MainActivity
import com.guillermohernandezpomares.storenba.ui.carrito.CarritoActivity
import com.guillermohernandezpomares.storenba.ui.perfil.PerfilActivity
import com.guillermohernandezpomares.storenba.utils.ProductoRepo
import com.guillermohernandezpomares.storenba.utils.Utiles

class DetalleEquipoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetalleEquipoBinding
    private val productoRepo: ProductoRepo = ProductoRepo()
    private val adapter: ProductoAdapter = ProductoAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleEquipoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /**
         *ProductosEquipo
         * Recycler de Productos
         * Filtro
         * Opcional: Decoración
         */


        val equipo = intent.getSerializableExtra("equipo") as? Equipo

        if (equipo != null) {
            setValores(equipo)
            // Llamada a la base datos y sacamos los productos
            setAdapterProducto(equipo)
        }


    }

    private fun setAdapterProducto(equipo: Equipo) {
        binding.rvProductosEquipo.setHasFixedSize(true)
        binding.rvProductosEquipo.layoutManager = LinearLayoutManager(this)
        binding.rvProductosEquipo.adapter = adapter
        observeData(equipo.nombre)
    }

    fun observeData(nombreEquipo: String) {
        productoRepo.getProductosPorEquipo().observe(this, Observer {
            // Adaptador de ProductosAdapter
            adapter.setProductos(it.filter { producto -> producto.equipo == nombreEquipo })
            binding.tvProductosEquipoVacio.visibility = View.INVISIBLE
            if (it.filter { producto -> producto.equipo == nombreEquipo }.isEmpty()) {
                binding.tvProductosEquipoVacio.visibility = View.VISIBLE
            }
            adapter.notifyDataSetChanged()
        })
    }

    fun setValores(equipo: Equipo) {

        binding.tvNombreEquipo.text = equipo.nombre
        Glide.with(this).load(equipo.imagen).into(binding.ivFotoEquipo)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return Utiles.onCreateOptionsMenu(menuInflater, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return Utiles.onOptionsItemSelected(this, item)
    }
}