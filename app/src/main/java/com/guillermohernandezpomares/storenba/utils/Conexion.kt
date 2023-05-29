package com.guillermohernandezpomares.storenba.utils

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.guillermohernandezpomares.storenba.model.Equipo
import com.guillermohernandezpomares.storenba.model.Producto

public  class Conexion {


    private val baseDatos = FirebaseFirestore.getInstance()
    private val almacenamiento = FirebaseStorage.getInstance()


    fun getEquipos() : LiveData<List<Equipo>>{

        val equipos : MutableLiveData<List<Equipo>> = MutableLiveData()

        baseDatos.collection(Constantes.EQUIPO)
            .get()
            .addOnSuccessListener { equiposDB ->
            val equiposList = ArrayList<Equipo>()
                equiposDB.forEach{
                    var equipo: Equipo = it.toObject()
                    equipo.id = it.id
                    equiposList.add(equipo)
                }
                equipos.value = equiposList
        }
        return equipos
    }



}