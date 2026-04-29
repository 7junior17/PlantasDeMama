package com.example.plantasdemam

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class VideoState(
    val titulo: String = "Video de la Planta",
    val videoId: String = "dQw4w9WgXcQ",
    val estaCargando: Boolean = false,
    val hayError: Boolean = false
)

class Actividad2ViewModel : ViewModel() {

    private val _estado = MutableStateFlow(VideoState())
    val estado: StateFlow<VideoState> = _estado.asStateFlow()

    fun cambiarVideo(titulo: String, videoId: String) {
        _estado.value = VideoState(
            titulo = titulo,
            videoId = videoId
        )
    }
}