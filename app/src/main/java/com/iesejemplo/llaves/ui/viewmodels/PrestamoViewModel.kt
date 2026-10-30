package com.iesejemplo.llaves.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed interface PrestamoUiState {
    object EscaneandoProfesor : PrestamoUiState
    object EscaneandoLlaveNfc : PrestamoUiState
    data class Confirmacion(
        val codigoProfesor: String,
        val nfcLlave: String,
        val aula: String
    ) : PrestamoUiState
    data class Exito(val mensaje: String) : PrestamoUiState
    data class Error(val error: String) : PrestamoUiState
}

class PrestamoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<PrestamoUiState>(PrestamoUiState.EscaneandoProfesor)
    val uiState: StateFlow<PrestamoUiState> = _uiState.asStateFlow()

    var codigoProfesorCapturado: String = ""
        private set

    var nfcLlaveCapturada: String = ""
        private set

    fun onBarcodeScanned(codigo: String) {
        if (codigo.isNotBlank() && _uiState.value is PrestamoUiState.EscaneandoProfesor) {
            codigoProfesorCapturado = codigo
            _uiState.update { PrestamoUiState.EscaneandoLlaveNfc }
        }
    }

    fun onNfcTagDetected(nfcUid: String) {
        nfcLlaveCapturada = nfcUid
        if (codigoProfesorCapturado.isNotEmpty()) {
            _uiState.update {
                PrestamoUiState.Confirmacion(
                    codigoProfesor = codigoProfesorCapturado,
                    nfcLlave = nfcLlaveCapturada,
                    aula = "BE205 - Berlín Planta 2"
                )
            }
        } else {
            _uiState.update { PrestamoUiState.EscaneandoProfesor }
        }
    }

    fun confirmarPrestamo() {
        _uiState.update {
            PrestamoUiState.Exito("Préstamo registrado correctamente")
        }
    }

    fun reiniciarFlujo() {
        codigoProfesorCapturado = ""
        nfcLlaveCapturada = ""
        _uiState.update { PrestamoUiState.EscaneandoProfesor }
    }
}
