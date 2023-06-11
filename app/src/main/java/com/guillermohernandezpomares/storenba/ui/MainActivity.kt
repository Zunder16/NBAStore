package com.guillermohernandezpomares.storenba.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.guillermohernandezpomares.storenba.R
import com.guillermohernandezpomares.storenba.databinding.ActivityMainBinding
import com.guillermohernandezpomares.storenba.ui.carrito.CarritoActivity
import com.guillermohernandezpomares.storenba.ui.equipo.EquipoAdapter
import com.guillermohernandezpomares.storenba.ui.perfil.PerfilActivity
import com.guillermohernandezpomares.storenba.utils.Conexion
import com.guillermohernandezpomares.storenba.utils.Constantes
import com.guillermohernandezpomares.storenba.utils.UsuarioRepo
import com.guillermohernandezpomares.storenba.utils.Utiles

class MainActivity : AppCompatActivity() {

    private val autorizacion: FirebaseAuth = FirebaseAuth.getInstance()
    private val baseDatos: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val conexion: Conexion = Conexion()
    private lateinit var binding: ActivityMainBinding
    private val adapterEste: EquipoAdapter = EquipoAdapter()
    private val adapterOeste: EquipoAdapter = EquipoAdapter()
    private val email = autorizacion.currentUser?.email
    private val usuarioRepo: UsuarioRepo = UsuarioRepo()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setAdapterConferencias()
        Log.i("Fin adapter", "Fin adapter")
        recogerDatosUsuario()
    }

    private fun recogerDatosUsuario() {
        usuarioRepo.getDatosUsuario(email!!).observe(this) {
            Constantes.DATOS_USUARIO = it
        }
    }

    private fun setAdapterConferencias() {
        binding.rvEquipoConferenciaEste.setHasFixedSize(true)
        val layoutManagerEste = GridLayoutManager(this, 5)
        binding.rvEquipoConferenciaEste.layoutManager = layoutManagerEste
        //binding.rvEquipo.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvEquipoConferenciaEste.adapter = adapterEste

        binding.rvEquipoConferenciaOeste.setHasFixedSize(true)
        val layoutManagerOeste = GridLayoutManager(this, 5)
        binding.rvEquipoConferenciaOeste.layoutManager = layoutManagerOeste
        //binding.rvEquipo.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvEquipoConferenciaOeste.adapter = adapterOeste


        observeData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return Utiles.onCreateOptionsMenu(menuInflater, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return Utiles.onOptionsItemSelected(this, item)
    }


    private fun observeData() {
        conexion.getEquipos().observe(this, Observer {
            adapterEste.setEquipos(it.filter { equipo -> equipo.conferencia == Constantes.CONFERENCIA_ESTE })
            adapterOeste.setEquipos(it.filter { equipo -> equipo.conferencia == Constantes.CONFERENCIA_OESTE })
            adapterEste.notifyDataSetChanged()
            adapterOeste.notifyDataSetChanged()
        })
    }
}