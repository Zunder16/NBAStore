package com.guillermohernandezpomares.storenba.utils

import com.google.firebase.firestore.FirebaseFirestore
import com.guillermohernandezpomares.storenba.model.Usuario
import com.guillermohernandezpomares.storenba.model.UsuarioInsertar

class UsuarioRepo {

    private val baseDatos = FirebaseFirestore.getInstance()

    fun getUsuario(){}
    fun insertUsuario(usuario: UsuarioInsertar){
        baseDatos.collection(Constantes.USUARIO).add(usuario)
    }
    fun modifyUsuario(idUsuario: String, usuario: UsuarioInsertar){
        baseDatos.collection(Constantes.USUARIO)
            .document(idUsuario)
            .set(usuario)
    }
    fun deleteUsuario(idUsuario: String){
        baseDatos.collection(Constantes.USUARIO)
            .document(idUsuario)
            .delete()
    }
    fun getDatosUsuario(email: String): Usuario {
        // Hacemos la query para sacar el usuario

        var usuario = Usuario()

        baseDatos.collection(Constantes.USUARIO).whereEqualTo("correo", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    for (document in querySnapshot.documents) {
                        usuario = document.toObject(Usuario::class.java)!!
                    }
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