package com.guillermohernandezpomares.storenba.model

data class ProductoCarrito(
    var id: String = "",
    var idProducto: String = "",
    var precio: Double = 0.0,
    var descripcion: String = "",
    var imagen: String = "",
    var cantidad: Int = 0,
    var talla: String = ""
) : java.io.Serializable

fun ProductoCarrito.toMap(): Map<String, Any> {
    val map = HashMap<String, Any>()
    map["id"] = id
    map["idProducto"] = idProducto
    map["precio"] = precio
    map["descripcion"] = descripcion
    map["imagen"] = imagen
    map["cantidad"] = cantidad
    map["talla"] = talla

    return map
}