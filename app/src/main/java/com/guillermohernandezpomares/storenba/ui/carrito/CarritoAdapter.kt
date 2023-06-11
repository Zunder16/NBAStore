package com.guillermohernandezpomares.storenba.ui.carrito

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.guillermohernandezpomares.storenba.R
import com.guillermohernandezpomares.storenba.databinding.ItemCarritoBinding
import com.guillermohernandezpomares.storenba.model.ProductoCarrito
import com.guillermohernandezpomares.storenba.model.ProductoCarritoInsertar
import com.guillermohernandezpomares.storenba.ui.pagar.PagarActivity
import com.guillermohernandezpomares.storenba.utils.CarritoRepo
import com.guillermohernandezpomares.storenba.utils.Constantes

class CarritoAdapter() : RecyclerView.Adapter<CarritoAdapter.ViewHolder>() {

    private var carrito: MutableList<ProductoCarrito> = ArrayList()
    private lateinit var carritoActivity: CarritoActivity

    fun setCarrito(carrito: MutableList<ProductoCarrito>, carritoActivity: CarritoActivity) {
        this.carrito = carrito
        this.carritoActivity = carritoActivity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemCarritoBinding.inflate(layoutInflater, parent, false).root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val carrito = carrito[position]
        holder.bind(carrito)
    }

    override fun getItemCount(): Int {
        return carrito.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemCarritoBinding.bind(view)
        private val carritoRepo: CarritoRepo = CarritoRepo()
        fun bind(carrito: ProductoCarrito) {
            Glide.with(itemView.context).load(carrito.imagen).into(binding.ivFotoProductoCarrito)
            binding.tvPrecioCarrito.text = String.format("%.2f€", carrito.precio)
            binding.tvDescripcionCarrito.text = carrito.descripcion

            spinnerCantidad(carrito.cantidad)
            spinnerTalla(carrito.talla)
            updateCarrito(carrito)
            deleteCarrito(carrito)

            binding.spCantidadCarrito.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        updateCarrito(carrito)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
            binding.spTallaCarrito.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        updateCarrito(carrito)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }

        }

        private fun deleteCarrito(carrito: ProductoCarrito) {

            binding.btnEliminar.setOnClickListener {
                dialogo(carrito)
            }

        }

        private fun updateCarrito(carrito: ProductoCarrito) {

            val nuevoCarrito: ProductoCarritoInsertar = ProductoCarritoInsertar()
            nuevoCarrito.imagen = carrito.imagen
            nuevoCarrito.precio = carrito.precio
            nuevoCarrito.descripcion = carrito.descripcion
            nuevoCarrito.idProducto = carrito.idProducto
            when (binding.spCantidadCarrito.selectedItem) {
                "1" -> nuevoCarrito.cantidad = 1
                "2" -> nuevoCarrito.cantidad = 2
                "3" -> nuevoCarrito.cantidad = 3
                "4" -> nuevoCarrito.cantidad = 4
                "5" -> nuevoCarrito.cantidad = 5
            }
            when (binding.spTallaCarrito.selectedItem) {
                "S" -> nuevoCarrito.talla = "S"
                "M" -> nuevoCarrito.talla = "M"
                "L" -> nuevoCarrito.talla = "L"
                "XL" -> nuevoCarrito.talla = "XL"
            }

            carritoRepo.updateDatosCarrito(Constantes.DATOS_USUARIO!!.id, carrito.id, nuevoCarrito)
        }

        fun spinnerCantidad(cantidad: Int) {
            val spOrdenar: Spinner = binding.spCantidadCarrito

            ArrayAdapter.createFromResource(
                itemView.context,
                R.array.desplegable_cantidad,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spOrdenar.adapter = adapter
            }
            when (cantidad) {
                1 -> binding.spCantidadCarrito.setSelection(0)
                2 -> binding.spCantidadCarrito.setSelection(1)
                3 -> binding.spCantidadCarrito.setSelection(2)
                4 -> binding.spCantidadCarrito.setSelection(3)
                5 -> binding.spCantidadCarrito.setSelection(4)
            }

        }

        fun spinnerTalla(talla: String) {
            val spOrdenar: Spinner = binding.spTallaCarrito

            ArrayAdapter.createFromResource(
                itemView.context,
                R.array.despegable_tallas,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spOrdenar.adapter = adapter
            }

            when (talla) {
                "S" -> binding.spTallaCarrito.setSelection(0)
                "M" -> binding.spTallaCarrito.setSelection(1)
                "L" -> binding.spTallaCarrito.setSelection(2)
                "XL" -> binding.spTallaCarrito.setSelection(3)
            }
        }

        private fun dialogo(carrito: ProductoCarrito) {
            val builder = AlertDialog.Builder(itemView.context)
            // Se crea el AlertDialog.
            builder.apply {
                // Se asigna un título.
                setTitle("Borrando producto del carrito")
                // Se asgina el cuerpo del mensaje.
                setMessage("¿Desea borrar el producto del carrito?")
                // Se define el comportamiento de los botones.
                setPositiveButton(android.R.string.ok) { _, _ ->
                    carritoRepo.deleteCarrito(Constantes.DATOS_USUARIO!!.id, carrito.id)
                    val intent = Intent(context, CarritoActivity::class.java)
                    context.startActivity(intent)
                }
                setNegativeButton(android.R.string.no) { _, _ ->
                    Toast.makeText(
                        itemView.context,
                        android.R.string.no,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            // Se muestra el AlertDialog.
            builder.show()
        }


    }
}