package com.guillermohernandezpomares.storenba.model

data class ProductoCarritoInsertar(
    var idProducto: String = "",
    var precio: Double = 0.0,
    var descripcion: String = "",
    var cantidad: Int = 0,
    var imagen: String = "",
    var talla: String = ""
) : java.io.Serializable