package com.example.plantasdemam

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Planta(
    @SerialName("nombre_cientifico") val nombreCientifico: String = "",
    @SerialName("nombre_comun") val nombreComun: String = "",
    @SerialName("familia") val familia: String = "",
    @SerialName("origen") val origen: String = "",
    @SerialName("manejo") val manejo: String = "",
    @SerialName("forma_de_vida") val formaDeVida: String = "",
    @SerialName("habitat") val habitat: String = "",
    @SerialName("imagen_url") val imagenUrl: String = "",
    @SerialName("descripcion_uso") val descripcionUso: String = ""
)