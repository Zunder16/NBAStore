package com.guillermohernandezpomares.storenba.ui.producto

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.guillermohernandezpomares.storenba.R
import com.guillermohernandezpomares.storenba.databinding.ActivityDetalleProductoBinding
import com.guillermohernandezpomares.storenba.model.Producto
import com.guillermohernandezpomares.storenba.model.ProductoCarritoInsertar
import com.guillermohernandezpomares.storenba.model.Usuario
import com.guillermohernandezpomares.storenba.ui.MainActivity
import com.guillermohernandezpomares.storenba.ui.carrito.CarritoActivity
import com.guillermohernandezpomares.storenba.ui.perfil.PerfilActivity
import com.guillermohernandezpomares.storenba.utils.CarritoRepo
import com.guillermohernandezpomares.storenba.utils.UsuarioRepo

class DetalleProductoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetalleProductoBinding
    private val usuarioRepo: UsuarioRepo = UsuarioRepo()
    private val carritoRepo: CarritoRepo = CarritoRepo()
    private val autorizacion: FirebaseAuth = FirebaseAuth.getInstance()
    private var usuario = Usuario()
    private val email = autorizacion.currentUser?.email

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        usuarioObserve()
        /**
         *
         *DetalleProducto*
         * Añadir botones de interacción: Añadir al carrito, añadir favoritos, tallas
         * Opcional: guía de tallas
         * */

        val producto = intent.extras?.getSerializable("producto") as? Producto
        datosVista(producto)

        binding.btnCarrito.setOnClickListener {
            addCarrito(producto)
        }


    }

    private fun usuarioObserve() {
        usuarioRepo.getDatosUsuario(email!!).observe(this) {
            usuario = it
        }
    }

    private fun addCarrito(producto: Producto?) {
        // Recogemos los datos y los ponemos en un objeto de ProductoCarrito
        val productoCarritoInsertar: ProductoCarritoInsertar = ProductoCarritoInsertar()
        if (producto != null) {
            productoCarritoInsertar.idProducto = producto.id
            productoCarritoInsertar.cantidad = binding.spCantidad.selectedItem.toString().toInt()
            when (binding.toggleButton.checkedButtonId) {
                binding.btnS.id -> productoCarritoInsertar.talla = "S"
                binding.btnM.id -> productoCarritoInsertar.talla = "M"
                binding.btnL.id -> productoCarritoInsertar.talla = "L"
                binding.btnXL.id -> productoCarritoInsertar.talla = "XL"
            }
            when (binding.spCantidad.selectedItem) {
                "1" -> productoCarritoInsertar.cantidad = 1
                "2" -> productoCarritoInsertar.cantidad = 2
                "3" -> productoCarritoInsertar.cantidad = 3
                "4" -> productoCarritoInsertar.cantidad = 4
                "5" -> productoCarritoInsertar.cantidad = 5
            }

            productoCarritoInsertar.descripcion = producto.descripcion
            productoCarritoInsertar.precio = producto.precio
            productoCarritoInsertar.imagen = producto.imagen

            if (usuario.id.isNotEmpty()) {
                carritoRepo.insertUsuarioProductoCarrito(usuario.id, productoCarritoInsertar)
                // navegamos al activity de carrito
                val intent = Intent(this, CarritoActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun datosVista(producto: Producto?) {
        // Imagen grande
        if (producto != null) {
            Glide.with(this).load(producto.imagen).into(binding.imagenDPProducto)
        }

        // Textos
        setSpinners()
        binding.tvDescripcion.text = producto?.descripcion
        binding.tvDPPrecio.text = String.format("%.2f€", producto!!.precio)

        // Comprobar si hay mas de una imagen
        if (producto != null) {
            if (producto.imagen2.isNotEmpty() || producto.imagen3.isNotEmpty()) {
                binding.clImagenes.visibility = View.VISIBLE
                Glide.with(this).load(producto.imagen).into(binding.imagen1)
                if (producto.imagen2.isNotEmpty()) {
                    Glide.with(this).load(producto.imagen2).into(binding.imagen2)
                }
                if (producto.imagen3.isNotEmpty()) {
                    Glide.with(this).load(producto.imagen3).into(binding.imagen3)
                }
            }
        }
    }

    private fun setSpinners() { //Funcion para poner datos a los spinners
        val spOrdenar: Spinner = binding.spCantidad

        ArrayAdapter.createFromResource(
            this,
            R.array.desplegable_cantidad,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spOrdenar.adapter = adapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu -> {
                // Acción para el botón 1 (lanzar Activity1)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
            R.id.perfil -> {
                // Acción para el botón 2 (lanzar Activity2)
                val intent = Intent(this, PerfilActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }

            R.id.carrito -> {
                // Acción para el botón 2 (lanzar Activity2)
                val intent = Intent(this, CarritoActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
            // Otros casos si tienes más botones en el menú

            else -> return super.onOptionsItemSelected(item)
        }
    }
}