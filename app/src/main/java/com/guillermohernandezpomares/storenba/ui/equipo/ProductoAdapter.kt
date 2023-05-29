package com.guillermohernandezpomares.storenba.ui.equipo

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.guillermohernandezpomares.storenba.databinding.ItemEquipoBinding
import com.guillermohernandezpomares.storenba.databinding.ItemProductoBinding
import com.guillermohernandezpomares.storenba.model.Equipo
import com.guillermohernandezpomares.storenba.model.Producto
import com.guillermohernandezpomares.storenba.utils.Constantes

class ProductoAdapter() : RecyclerView.Adapter<ProductoAdapter.ViewHolder>() {

    private var productos: List<Producto> = ArrayList()

    fun setProductos(productos: List<Producto>){
        this.productos = productos
        Log.i("Fallo en setters", "Fallo en setters")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemProductoBinding.inflate(layoutInflater, parent, false).root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = productos[position]
        holder.bind(producto)
    }

    override fun getItemCount(): Int {
        return productos.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemProductoBinding.bind(view)
        fun bind(producto: Producto) {

            with(binding){
                Glide.with(itemView.context).load(Constantes.IMAGEN_PRODUCTO_DEFECTO).into(ivFotoProducto)
                if (producto.imagen.isNotEmpty()){
                    Glide.with(itemView.context).load(producto.imagen).into(ivFotoProducto)
                }
                tvPrecio.text = producto.precio.toString() + "€"
                tvDescripcion.text = producto.descripcion
            }



        }


    }
}