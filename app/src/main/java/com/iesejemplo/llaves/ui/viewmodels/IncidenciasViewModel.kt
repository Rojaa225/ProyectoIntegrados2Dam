package com.iesejemplo.llaves.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class IncidenciaFormState(
    val llave: String = "Llave Aula 102",
    val tipo: String = "Perdida",
    val descripcion: String = "",
    val enviada: Boolean = false,
    val mensajeConfirmacion: String = ""
)

class IncidenciasViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(IncidenciaFormState())
    val uiState: StateFlow<IncidenciaFormState> = _uiState.asStateFlow()

    fun updateLlave(llave: String) {
        _uiState.update { it.copy(llave = llave, enviada = false) }
    }

    fun updateTipo(tipo: String) {
        _uiState.update { it.copy(tipo = tipo, enviada = false) }
    }

    fun updateDescripcion(descripcion: String) {
        _uiState.update { it.copy(descripcion = descripcion, enviada = false) }
    }

    fun enviarReporte() {
        val currentState = _uiState.value
        if (currentState.llave.isNotBlank() && currentState.descripcion.isNotBlank()) {
            _uiState.update {
                it.copy(
                    enviada = true,
                    mensajeConfirmacion = "Reporte enviado con éxito para ${currentState.llave} (${currentState.tipo})"
                )
            }
        }
    }

    fun resetMensaje() {
        _uiState.update { it.copy(enviada = false, mensajeConfirmacion = "") }
    }
}
