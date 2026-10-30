package com.iesejemplo.llaves.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.iesejemplo.llaves.ui.screens.Prestamo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LoansUiState(
    val prestamos: List<Prestamo> = listOf(
        Prestamo("Llave Aula 102", "Marta Gómez", "MG", "Desde las 08:15"),
        Prestamo("Llave Gimnasio", "Luis Fernández", "LF", "Desde las 09:00"),
        Prestamo("Llave Aula 201", "Carmen Ruiz", "CR", "Desde ayer, 17:30", retrasado = true)
    )
)

class LoansViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoansUiState())
    val uiState: StateFlow<LoansUiState> = _uiState.asStateFlow()

    fun devolverPrestamo(prestamo: Prestamo) {
        _uiState.update { state ->
            state.copy(prestamos = state.prestamos.filter { it != prestamo })
        }
    }

    fun agregarPrestamo(llave: String, persona: String, iniciales: String, hora: String) {
        val nuevo = Prestamo(llave, persona, iniciales, hora)
        _uiState.update { state ->
            state.copy(prestamos = state.prestamos + nuevo)
        }
    }
}
