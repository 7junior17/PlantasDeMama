package com.example.plantasdemam

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlantasViewModel : ViewModel() {

    var plantas by mutableStateOf<List<Planta>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    var busqueda by mutableStateOf("")
        private set

    var categoriaActiva by mutableStateOf("Todas")
        private set

    val plantasFiltradas: List<Planta>
        get() = if (busqueda.isEmpty()) {
            plantas
        } else {
            plantas.filter { planta ->
                planta.nombreComun.contains(busqueda, ignoreCase = true) ||
                        planta.nombreCientifico.contains(busqueda, ignoreCase = true) ||
                        planta.familia.contains(busqueda, ignoreCase = true) ||
                        planta.origen.contains(busqueda, ignoreCase = true) ||
                        planta.manejo.contains(busqueda, ignoreCase = true) ||
                        planta.formaDeVida.contains(busqueda, ignoreCase = true) ||
                        planta.habitat.contains(busqueda, ignoreCase = true)
            }
        }

    fun onBusquedaChange(query: String) {
        busqueda = query

        if (query.isNotEmpty()) {
            cargarTodasLasPlantas()
        } else {
            cargarPorCategoria(categoriaActiva)
        }
    }

    fun cargarPlantasIniciales() {
        categoriaActiva = "Todas"
        busqueda = ""
        isLoading = false
        error = null

        plantas = listOf(
            Planta(
                nombreComun = "Manzanilla",
                nombreCientifico = "Matricaria chamomilla",
                familia = "Asteraceae",
                origen = "Europa y Asia",
                manejo = "cultivada",
                formaDeVida = "hierba",
                habitat = "huerto",
                imagenUrl = "local:manzanilla",
                descripcionUso = "Calma la digestión y reduce la ansiedad."
            ),
            Planta(
                nombreComun = "Lavanda",
                nombreCientifico = "Lavandula angustifolia",
                familia = "Lamiaceae",
                origen = "Mediterráneo",
                manejo = "cultivada",
                formaDeVida = "arbusto",
                habitat = "huerto",
                imagenUrl = "local:lavanda",
                descripcionUso = "Reduce el estrés y mejora el sueño."
            ),
            Planta(
                nombreComun = "Jengibre",
                nombreCientifico = "Zingiber officinale",
                familia = "Zingiberaceae",
                origen = "Asia",
                manejo = "cultivada",
                formaDeVida = "hierba",
                habitat = "huerto",
                imagenUrl = "local:jengibre",
                descripcionUso = "Antiinflamatorio natural. Alivia náuseas."
            ),
            Planta(
                nombreComun = "Aloe Vera",
                nombreCientifico = "Aloe barbadensis",
                familia = "Xanthorrhoeaceae",
                origen = "África",
                manejo = "cultivada",
                formaDeVida = "hierba",
                habitat = "huerto",
                imagenUrl = "local:aloe_vera",
                descripcionUso = "Cicatriza y regenera la piel."
            )
        )
    }

    fun cargarPorCategoria(categoria: String) {
        categoriaActiva = categoria
        busqueda = ""

        if (categoria == "Todas") {
            cargarPlantasIniciales()
            return
        }

        viewModelScope.launch {
            isLoading = true
            error = null

            try {
                val resultado = withContext(Dispatchers.IO) {
                    when (categoria) {
                        "Cultivada" -> {
                            supabase.from("plantas").select {
                                filter {
                                    ilike("manejo", "%cultivada%")
                                }
                            }.decodeList<Planta>()
                        }

                        "Silvestre" -> {
                            supabase.from("plantas").select {
                                filter {
                                    ilike("manejo", "%silvestre%")
                                }
                            }.decodeList<Planta>()
                        }

                        "Hierba" -> {
                            supabase.from("plantas").select {
                                filter {
                                    ilike("forma_de_vida", "%hierba%")
                                }
                            }.decodeList<Planta>()
                        }

                        "Árbol" -> {
                            supabase.from("plantas").select {
                                filter {
                                    ilike("forma_de_vida", "%árbol%")
                                }
                            }.decodeList<Planta>()
                        }

                        "Arbusto" -> {
                            supabase.from("plantas").select {
                                filter {
                                    ilike("forma_de_vida", "%arbusto%")
                                }
                            }.decodeList<Planta>()
                        }

                        else -> {
                            supabase.from("plantas").select().decodeList<Planta>()
                        }
                    }
                }

                plantas = resultado

            } catch (e: Exception) {
                error = "Error al cargar plantas: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun cargarTodasLasPlantas() {
        viewModelScope.launch {
            isLoading = true
            error = null

            try {
                val resultado = withContext(Dispatchers.IO) {
                    supabase
                        .from("plantas")
                        .select()
                        .decodeList<Planta>()
                }

                plantas = resultado

            } catch (e: Exception) {
                error = "Error al cargar plantas: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}