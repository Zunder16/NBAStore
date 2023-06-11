package com.guillermohernandezpomares.storenba.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.guillermohernandezpomares.storenba.model.Pedido
import com.guillermohernandezpomares.storenba.model.PedidoInsertar
import com.guillermohernandezpomares.storenba.model.ProductoCarrito
import com.guillermohernandezpomares.storenba.model.toMap

class PedidoRepo {

    private val baseDatos = FirebaseFirestore.getInstance()
    private val almacenamiento = FirebaseStorage.getInstance()

    fun deletePedido(idUsuario: String, idPedido: String) {

        baseDatos.collection(Constantes.USUARIO)
            .document(idUsuario)
            .collection(Constantes.PEDIDO)
            .document(idPedido)
            .delete()

    }

    fun insertPedido(
        idUsuario: String,
        pedido: PedidoInsertar,
        listaProductos: List<ProductoCarrito>
    ) {
        baseDatos.collection(Constantes.USUARIO)
            .document(idUsuario)
            .collection(Constantes.PEDIDO)
            .add(pedido).addOnSuccessListener {
                documentReference ->
                listaProductos.forEach {
                    documentReference.collection(Constantes.PRODUCTOS).add(it.toMap())
                }
                limpiarCarrito(idUsuario)
            }

    }

    private fun limpiarCarrito(idUsuario: String) {
        val collectionRef = baseDatos.collection(Constantes.USUARIO)
            .document(idUsuario)
            .collection(Constantes.CARRITO)

        collectionRef.get().addOnSuccessListener { querySnapshot ->
            val batch = baseDatos.batch()

            for (document in querySnapshot) {
                batch.delete(document.reference)
            }

            batch.commit().addOnSuccessListener {
                // Operación de eliminación exitosa
            }.addOnFailureListener { e ->
                // Manejo de errores
            }
        }
    }

    fun modifyPedido(idUsuario: String, idPedido: String, pedido: PedidoInsertar) {
        baseDatos.collection(Constantes.USUARIO)
            .document(idUsuario)
            .collection(Constantes.PEDIDO)
            .document(idPedido)
            .set(pedido)
    }


    fun getPedidosUsuario(idUsuario: String): LiveData<List<Pedido>> {
        val pedidos: MutableLiveData<List<Pedido>> = MutableLiveData()
        baseDatos.collection(Constantes.USUARIO)
            .document(idUsuario)
            .collection(Constantes.PEDIDO)
            .get()
            .addOnSuccessListener { pedidosDB ->
                var pedidosList = ArrayList<Pedido>()
                pedidosDB.forEach {
                    var pedido: Pedido = it.toObject()
                    pedido.id = it.id
                    pedidosList.add((pedido))
                }
                pedidos.value = pedidosList
            }
        return pedidos

    }

    fun getPedidoUsuario(idUsuario: String, idPedido: String): LiveData<Pedido> {

        val pedido: MutableLiveData<Pedido> = MutableLiveData()

        baseDatos.collection(Constantes.USUARIO)
            .document(idUsuario)
            .collection(Constantes.PEDIDO)
            .document(idPedido)
            .get()
            .addOnSuccessListener { pedidoDB ->
                var pedidoRepo: Pedido? = pedidoDB.toObject()
                pedido.value = pedidoRepo
                if (pedidoRepo == null) {
                    pedidoRepo = Pedido()
                    pedido.value = pedidoRepo
                }
            }

        return pedido
    }
}