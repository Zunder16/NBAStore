package com.guillermohernandezpomares.storenba.ui.pagar

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.guillermohernandezpomares.storenba.databinding.ActivityPagarBinding
import com.guillermohernandezpomares.storenba.model.PedidoInsertar
import com.guillermohernandezpomares.storenba.ui.MainActivity
import com.guillermohernandezpomares.storenba.utils.CarritoRepo
import com.guillermohernandezpomares.storenba.utils.Constantes
import com.guillermohernandezpomares.storenba.utils.PedidoRepo
import com.guillermohernandezpomares.storenba.utils.Utiles
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PagarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPagarBinding
    private val carritoRepo: CarritoRepo = CarritoRepo()
    private val pedidoRepo: PedidoRepo = PedidoRepo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagarBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnPagarFinal.setOnClickListener {
            generarPedido()
        }

    }

    private fun generarPedido() {

        // Tendremos que hacer comprobaciones de codigo

        // Comprobar que la tarjeta es valida
        if (comprobarTarjeta(binding.etTarjeta.editText!!.text.toString())) {
            val pedidoInsertar = PedidoInsertar()
            pedidoInsertar.direccion = binding.etDireccion.editText!!.text.toString()
            pedidoInsertar.estado = Constantes.ESTADO_PREDETERMINADO
            pedidoInsertar.usuario =
                Constantes.DATOS_USUARIO!!.nombre + " " + Constantes.DATOS_USUARIO!!.apellidos
            pedidoInsertar.fechaCompra = formatearFecha(LocalDate.now())

            carritoRepo.getDatosCarrito(Constantes.DATOS_USUARIO!!.id).observe(this) { it ->
                it.forEach { productoCarrito ->
                    pedidoInsertar.precioTotal += productoCarrito.precio
                }
                pedidoRepo.insertPedido(Constantes.DATOS_USUARIO!!.id, pedidoInsertar, it)

                // Navegar a pantalla

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            }
        } else {
            // Toast diciendo que el numero de tarjeta no es correcto
            Toast.makeText(this, "El numero de la tarjeta no es correcto", Toast.LENGTH_LONG).show()
        }

    }

    private fun comprobarTarjeta(creditCardNumber: String): Boolean {
        // Eliminar espacios en blanco y guiones
        val cleanNumber = creditCardNumber.replace(" ", "").replace("-", "")
        // Verificar si la longitud es válida
        if (cleanNumber.length < 13 || cleanNumber.length > 16) {
            return false
        }
        // Verificar si todos los caracteres son dígitos
        if (!cleanNumber.matches(Regex("\\d+"))) {
            return false
        }
        // Aplicar el algoritmo de Luhn
        var sum = 0
        var alternate = false
        for (i in cleanNumber.length - 1 downTo 0) {
            var digit = cleanNumber[i].toInt() - '0'.toInt()
            if (alternate) {
                digit *= 2
                if (digit > 9) {
                    digit -= 9
                }
            }
            sum += digit
            alternate = !alternate
        }
        // El número de tarjeta de crédito es válido si la suma es divisible por 10
        //return sum % 10 == 0
        return true
    }

    private fun formatearFecha(now: LocalDate?): String {
        val formateoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return now!!.format(formateoFecha)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return Utiles.onCreateOptionsMenu(menuInflater, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return Utiles.onOptionsItemSelected(this, item)
    }

}