package com.iesejemplo.llaves.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.iesejemplo.llaves.ui.viewmodels.IncidenciasViewModel

private val Azul = Color(0xFF193F76)
private val Fondo = Color(0xFFF2F5FB)
private val Naranja = Color(0xFFFA8C24)
private val Texto = Color(0xFF142238)
private val Borde = Color(0xFFC4D3E7)

@Composable
fun IncidentsScreen(
    viewModel: IncidenciasViewModel = viewModel(),
    onSend: ((String, String, String) -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
    ) {
        // CABECERA AZUL
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Azul,
                    shape = RoundedCornerShape(
                        bottomStart = 24.dp,
                        bottomEnd = 24.dp
                    )
                )
                .statusBarsPadding()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 28.dp,
                    bottom = 18.dp
                )
        ) {
            Text(
                text = "Reportar incidencia",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Avisa si una llave se ha perdido o roto",
                fontSize = 16.sp,
                color = Color(0xFFD2DDED)
            )
        }

        // CONTENIDO DE LA PANTALLA
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (uiState.enviada) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFD4EDDA)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF155724),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = uiState.mensajeConfirmacion,
                            color = Color(0xFF155724),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // LLAVE AFECTADA
            Text(
                text = "Llave afectada",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Texto
            )

            OutlinedTextField(
                value = uiState.llave,
                onValueChange = { viewModel.updateLlave(it) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Borde,
                    unfocusedBorderColor = Borde,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            // TIPO DE INCIDENCIA
            Text(
                text = "Tipo de incidencia",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Texto
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // BOTÓN PERDIDA
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .background(
                            color = if (uiState.tipo == "Perdida") Azul else Color.White,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = if (uiState.tipo == "Perdida") Azul else Borde,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable { viewModel.updateTipo("Perdida") },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Perdida",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (uiState.tipo == "Perdida") Color.White else Texto
                    )
                }

                // BOTÓN ROTA
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .background(
                            color = if (uiState.tipo == "Rota") Azul else Color.White,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = if (uiState.tipo == "Rota") Azul else Borde,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable { viewModel.updateTipo("Rota") },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Rota",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (uiState.tipo == "Rota") Color.White else Texto
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // DESCRIPCIÓN
            Text(
                text = "Descripción",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Texto
            )

            OutlinedTextField(
                value = uiState.descripcion,
                onValueChange = { viewModel.updateDescripcion(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                placeholder = {
                    Text(text = "Cuéntanos qué ha pasado")
                },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Borde,
                    unfocusedBorderColor = Borde,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            // BOTÓN ENVIAR REPORTE
            Button(
                onClick = {
                    viewModel.enviarReporte()
                    onSend?.invoke(uiState.llave, uiState.tipo, uiState.descripcion)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Naranja
                )
            ) {
                Text(
                    text = "Enviar reporte",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Texto
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewIncidentsScreen() {
    IncidentsScreen()
}
