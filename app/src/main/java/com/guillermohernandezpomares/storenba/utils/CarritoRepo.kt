package com.guillermohernandezpomares.storenba.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.guillermohernandezpomares.storenba.model.ProductoCarrito
import com.guillermohernandezpomares.storenba.model.ProductoCarritoInsertar

class CarritoRepo {
    private val baseDatos = FirebaseFirestore.getInstance()

    fun insertUsuarioProductoCarrito(
        idUsuario: String,
        productoCarritoInsertar: ProductoCarritoInsertar
    ) {
        baseDatos.collection(Constantes.USUARIO)
            .document(idUsuario)
            .collection(Constantes.CARRITO)
            .add(productoCarritoInsertar)
    }

    fun getDatosCarrito(idUsuario: String): LiveData<MutableList<ProductoCarrito>> {
        val carrito: MutableLiveData<MutableList<ProductoCarrito>> = MutableLiveData()

        baseDatos.collection(Constantes.USUARIO)
            .document(idUsuario)
            .collection(Constantes.CARRITO)
            .get()
            .addOnSuccessListener { carritoItems ->
                val carritoList = mutableListOf<ProductoCarrito>()
                carritoItems.forEach {
                    val productoCarrito: ProductoCarrito = it.toObject()
                    productoCarrito.id = it.id
                    carritoList.add(productoCarrito)
                }
                carrito.value = carritoList
            }

        return carrito
    }

    fun updateDatosCarrito(
        idUsuario: String,
        idCarrito: String,
        carritoInsertar: ProductoCarritoInsertar
    ) {

        baseDatos.collection(Constantes.USUARIO)
            .document(idUsuario)
            .collection(Constantes.CARRITO)
            .document(idCarrito)
            .set(carritoInsertar)

    }

    fun deleteCarrito(idUsuario: String, idCarrito: String) {
        baseDatos.collection(Constantes.USUARIO)
            .document(idUsuario)
            .collection(Constantes.CARRITO)
            .document(idCarrito)
            .delete()
    }
}