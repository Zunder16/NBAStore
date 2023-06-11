package com.guillermohernandezpomares.storenba.ui.perfil.pedido

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.guillermohernandezpomares.storenba.databinding.ItemCarritoBinding
import com.guillermohernandezpomares.storenba.databinding.ItemPedidoBinding
import com.guillermohernandezpomares.storenba.model.Pedido
import com.guillermohernandezpomares.storenba.utils.PedidoRepo

class PedidoAdapter : RecyclerView.Adapter<PedidoAdapter.ViewHolder>() {

    private var pedidos: List<Pedido> = ArrayList()

    fun setPedidos(pedidos: List<Pedido>) {
        this.pedidos = pedidos
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemPedidoBinding.inflate(layoutInflater, parent, false).root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pedido = pedidos[position]
        holder.bind(pedido)
    }

    override fun getItemCount(): Int {
        return pedidos.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemPedidoBinding.bind(view)
        private val pedidoRepo: PedidoRepo = PedidoRepo()
        fun bind(pedido: Pedido) {

            with(binding){
                tvDireccionPedido.text = pedido.direccion
                tvEstadoPedido.text = pedido.estado
                tvIdPedido.text = pedido.id
                tvFechaPedido.text = pedido.fechaCompra
                tvPrecioPedido.text = pedido.precioTotal.toString()
            }


        }
    }

}