package com.guillermohernandezpomares.storenba.model

data class Producto(
    var id: String = "",
    var nombre: String = "",
    var precio: Double = 0.0,
    var descripcion: String = "",
    var equipo: String = "",
    var dorsal: Int = 0,
    var imagen: String = "",
    var imagen2: String = "",
    var imagen3: String = ""
) : java.io.Serializable