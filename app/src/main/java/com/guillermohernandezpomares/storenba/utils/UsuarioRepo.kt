package com.guillermohernandezpomares.storenba.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.guillermohernandezpomares.storenba.model.Usuario
import com.guillermohernandezpomares.storenba.model.UsuarioInsertar

class UsuarioRepo {

    private val baseDatos = FirebaseFirestore.getInstance()

    fun getUsuario() {}
    fun insertUsuario(usuario: UsuarioInsertar) {
        baseDatos.collection(Constantes.USUARIO).add(usuario)
    }

    fun modifyUsuario(idUsuario: String, usuario: UsuarioInsertar) {
        baseDatos.collection(Constantes.USUARIO)
            .document(idUsuario)
            .set(usuario)
    }

    fun deleteUsuario(idUsuario: String) {
        baseDatos.collection(Constantes.USUARIO)
            .document(idUsuario)
            .delete()
    }

    fun getDatosUsuario(email: String): LiveData<Usuario> {
        val usuario: MutableLiveData<Usuario> = MutableLiveData()
        baseDatos.collection(Constantes.USUARIO).whereEqualTo("correo", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                var usuarioRepo = Usuario()
                if (!querySnapshot.isEmpty) {
                    for (document in querySnapshot.documents) {
                        usuarioRepo = document.toObject(Usuario::class.java)!!
                        usuarioRepo.id = document.id
                    }
                    usuario.value = usuarioRepo
                } else {
                    // No se encontraron documentos que coincidan con el campo "email"
                }
            }
            .addOnFailureListener { exception ->
                // Manejar cualquier error que ocurra durante la consulta
            }
        return usuario
    }


}