package com.guillermohernandezpomares.storenba.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.guillermohernandezpomares.storenba.model.Producto

class ProductoRepo {

    private val baseDatos = FirebaseFirestore.getInstance()
    private val almacenamiento = FirebaseStorage.getInstance()

    fun getProductos() : LiveData<List<Producto>>{

        val productos : MutableLiveData<List<Producto>> = MutableLiveData()

        baseDatos.collection(Constantes.PRODUCTOS)
            .get()
            .addOnSuccessListener { productoDB ->
                val productoList = ArrayList<Producto>()
                productoDB.forEach{
                    var producto: Producto = it.toObject()
                    producto.id = it.id
                    productoList.add((producto))
                }
                productos.value = productoList
            }
        return productos
    }
    fun getProducto(id: String) : LiveData<Producto>{
        val producto : MutableLiveData<Producto> = MutableLiveData()

        baseDatos.collection(Constantes.PRODUCTOS)
            .document(id)
            .get()
            .addOnSuccessListener { productoDB ->
                var productoSnapshot: Producto? = productoDB.toObject()
                producto.value = productoSnapshot
                if (productoSnapshot == null){
                    productoSnapshot = Producto()
                    producto.value = productoSnapshot
                }
            }
        return producto
    }



    fun getProductosPorEquipo() : LiveData<List<Producto>> {

        val productos : MutableLiveData<List<Producto>> = MutableLiveData()

        baseDatos.collection(Constantes.PRODUCTOS)
            .get()
            .addOnSuccessListener { productoDB ->
                val productoList = ArrayList<Producto>()
                productoDB.forEach{
                    val producto: Producto = it.toObject()
                    producto.id = it.id
                    productoList.add(producto)
                }
                productos.value = productoList
            }
        return productos

    }

}