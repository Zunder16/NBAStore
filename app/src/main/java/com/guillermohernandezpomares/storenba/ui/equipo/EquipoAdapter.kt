package com.guillermohernandezpomares.storenba.ui.equipo

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.guillermohernandezpomares.storenba.databinding.ItemEquipoBinding
import com.guillermohernandezpomares.storenba.model.Equipo

class EquipoAdapter() : RecyclerView.Adapter<EquipoAdapter.ViewHolder>() {

    private var equipos: List<Equipo> = ArrayList()

    fun setEquipos(equipos: List<Equipo>){
        this.equipos = equipos
        Log.i("Fallo en setters", "Fallo en setters")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemEquipoBinding.inflate(layoutInflater, parent, false).root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val equipo = equipos[position]
        holder.bind(equipo)
    }

    override fun getItemCount(): Int {
        return equipos.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemEquipoBinding.bind(view)
        fun bind(equipo: Equipo) {
                Glide.with(itemView.context).load(equipo.imagen).into(binding.ivImagen)
                binding.ivImagen.setOnClickListener{
                    val intent = Intent(itemView.context, DetalleEquipoActivity::class.java)
                    intent.putExtra("equipo", equipo)
                    itemView.context.startActivity(intent)
                }
        }


    }



}