package com.guillermohernandezpomares.storenba.ui.perfil.pedido

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.guillermohernandezpomares.storenba.R
import com.guillermohernandezpomares.storenba.databinding.ActivityPedidoBinding
import com.guillermohernandezpomares.storenba.ui.MainActivity
import com.guillermohernandezpomares.storenba.ui.carrito.CarritoActivity
import com.guillermohernandezpomares.storenba.ui.carrito.CarritoAdapter
import com.guillermohernandezpomares.storenba.ui.perfil.PerfilActivity
import com.guillermohernandezpomares.storenba.utils.CarritoRepo
import com.guillermohernandezpomares.storenba.utils.Constantes
import com.guillermohernandezpomares.storenba.utils.PedidoRepo
import com.guillermohernandezpomares.storenba.utils.Utiles

class PedidoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPedidoBinding
    private val adapter: PedidoAdapter = PedidoAdapter()
    private val pedidoRepo: PedidoRepo = PedidoRepo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setAdapterPedido()
        observeData()

    }

    /*
    * Recycler de los pedidos del usuario
    *
    * */

    private fun setAdapterPedido() {
        binding.rvPedido.setHasFixedSize(true)
        binding.rvPedido.layoutManager = LinearLayoutManager(this)
        binding.rvPedido.adapter = adapter
    }

    private fun observeData() {
        pedidoRepo.getPedidosUsuario(Constantes.DATOS_USUARIO!!.id).observe(this) {
            adapter.setPedidos(it)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return Utiles.onCreateOptionsMenu(menuInflater, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return Utiles.onOptionsItemSelected(this, item)
    }

}