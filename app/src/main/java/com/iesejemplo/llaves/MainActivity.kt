package com.iesejemplo.llaves

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iesejemplo.llaves.ui.screens.*
import com.iesejemplo.llaves.ui.theme.AppLlavesTheme
import com.iesejemplo.llaves.ui.theme.Navy
import com.iesejemplo.llaves.ui.theme.Orange
import com.iesejemplo.llaves.ui.viewmodels.IncidenciasViewModel
import com.iesejemplo.llaves.ui.viewmodels.LoansViewModel
import com.iesejemplo.llaves.ui.viewmodels.PrestamoViewModel

class MainActivity : ComponentActivity() {

    private var nfcAdapter: NfcAdapter? = null
    private val prestamoViewModel: PrestamoViewModel by viewModels()
    private val incidenciasViewModel: IncidenciasViewModel by viewModels()
    private val loansViewModel: LoansViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilitar diseño Edge-to-Edge obligatorio
        enableEdgeToEdge()

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        setContent {
            AppLlavesTheme {
                MainLanzadera(
                    prestamoViewModel = prestamoViewModel,
                    incidenciasViewModel = incidenciasViewModel,
                    loansViewModel = loansViewModel
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Activar lectura NFC en Foreground Dispatch sin pulsar botones
        nfcAdapter?.let { adapter ->
            val intent = Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_MUTABLE
            )
            adapter.enableForegroundDispatch(this, pendingIntent, null, null)
        }
    }

    override fun onPause() {
        super.onPause()
        // Desactivar lectura para ahorrar batería al pasar a segundo plano
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // Detección automática de tag NFC
        val action = intent.action
        if (NfcAdapter.ACTION_TAG_DISCOVERED == action ||
            NfcAdapter.ACTION_TECH_DISCOVERED == action ||
            NfcAdapter.ACTION_NDEF_DISCOVERED == action
        ) {
            val tagId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)
            val hexId = tagId?.joinToString(":") { "%02X".format(it) } ?: "UNKNOWN_TAG"

            // Notificar al ViewModel
            prestamoViewModel.onNfcTagDetected(hexId)
        }
    }
}

enum class Pantalla {
    MENU,
    SCANNER_PRESTAMO,
    BUSCAR_LLAVE,
    LLAVES,
    LOANS,
    LOGIN,
    INCIDENCIAS
}

@Composable
fun MainLanzadera(
    prestamoViewModel: PrestamoViewModel,
    incidenciasViewModel: IncidenciasViewModel,
    loansViewModel: LoansViewModel
) {
    var pantallaActual by remember { mutableStateOf(Pantalla.MENU) }

    Box(modifier = Modifier.fillMaxSize()) {
        when (pantallaActual) {
            Pantalla.MENU -> MenuLanzadera(onSeleccionar = { pantallaActual = it })
            Pantalla.SCANNER_PRESTAMO -> ScannerPrestamoScreen(viewModel = prestamoViewModel)
            Pantalla.BUSCAR_LLAVE -> BuscarLlaveScreen()
            Pantalla.LLAVES -> LlavesScreen()
            Pantalla.LOANS -> LoansScreen(viewModel = loansViewModel)
            Pantalla.LOGIN -> LoginScreen()
            Pantalla.INCIDENCIAS -> IncidentsScreen(viewModel = incidenciasViewModel)
        }

        // Botón flotante para regresar a la Lanzadera desde cualquier pantalla
        if (pantallaActual != Pantalla.MENU) {
            FloatingActionButton(
                onClick = { pantallaActual = Pantalla.MENU },
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(16.dp)
                    .align(Alignment.TopStart),
                containerColor = Orange,
                contentColor = Color.White
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    Text("Lanzadera", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun MenuLanzadera(onSeleccionar: (Pantalla) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F264A))
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))
        Text(
            text = "🚀 Lanzadera de Pantallas",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Selecciona una ventana para probarla",
            color = Color(0xFFC4D3E7),
            fontSize = 15.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 28.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            BotonLanzadera(
                titulo = "1. AutoScan Préstamo (CameraX + NFC)",
                subtitulo = "Apertura directa de cámara y tag NFC",
                icono = Icons.Default.QrCodeScanner,
                onClick = { onSeleccionar(Pantalla.SCANNER_PRESTAMO) }
            )

            BotonLanzadera(
                titulo = "2. BuscarLlaveScreen",
                subtitulo = "Buscador y escáner NFC",
                icono = Icons.Default.Search,
                onClick = { onSeleccionar(Pantalla.BUSCAR_LLAVE) }
            )

            BotonLanzadera(
                titulo = "3. Llaves",
                subtitulo = "Estado de llaves del centro",
                icono = Icons.Default.VpnKey,
                onClick = { onSeleccionar(Pantalla.LLAVES) }
            )

            BotonLanzadera(
                titulo = "4. LoansScreen",
                subtitulo = "Gestión de préstamos activos (ViewModel)",
                icono = Icons.AutoMirrored.Filled.FormatListBulleted,
                onClick = { onSeleccionar(Pantalla.LOANS) }
            )

            BotonLanzadera(
                titulo = "5. Login",
                subtitulo = "Pantalla de inicio de sesión",
                icono = Icons.Default.Lock,
                onClick = { onSeleccionar(Pantalla.LOGIN) }
            )

            BotonLanzadera(
                titulo = "6. PantallaIncidencias",
                subtitulo = "Reportar llaves rotas o perdidas (ViewModel)",
                icono = Icons.Default.Warning,
                onClick = { onSeleccionar(Pantalla.INCIDENCIAS) }
            )
        }
    }
}

@Composable
private fun BotonLanzadera(
    titulo: String,
    subtitulo: String,
    icono: ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Navy
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = Orange,
                modifier = Modifier.size(28.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Navy
                )
                Text(
                    text = subtitulo,
                    fontSize = 13.sp,
                    color = Color(0xFF5B6B82)
                )
            }
        }
    }
}
