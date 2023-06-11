package com.guillermohernandezpomares.storenba.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.guillermohernandezpomares.storenba.R
import com.guillermohernandezpomares.storenba.ui.MainActivity
import com.guillermohernandezpomares.storenba.ui.carrito.CarritoActivity
import com.guillermohernandezpomares.storenba.ui.perfil.PerfilActivity

class Utiles {

    companion object {
        fun onCreateOptionsMenu(menuInflater: MenuInflater, menu: Menu): Boolean {
            menuInflater.inflate(R.menu.menu_main, menu)
            return true
        }

        fun onOptionsItemSelected(context: Context, item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.menu -> {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                    (context as Activity).finish()
                    return true
                }
                R.id.perfil -> {
                    val intent = Intent(context, PerfilActivity::class.java)
                    context.startActivity(intent)
                    (context as Activity).finish()
                    return true
                }
                R.id.carrito -> {
                    val intent = Intent(context, CarritoActivity::class.java)
                    context.startActivity(intent)
                    (context as Activity).finish()
                    return true
                }
                // Otros casos si tienes más botones en el menú

                else -> return false
            }
        }
    }
}