package com.guillermohernandezpomares.storenba.model

class PedidoInsertar(
    var usuario: String = "",
    var fechaCompra: String = "",
    var direccion: String = "",
    var precioTotal: Double = 0.0,
    var estado: String = ""
)