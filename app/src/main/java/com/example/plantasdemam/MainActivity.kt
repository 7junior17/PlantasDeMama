package com.example.plantasdemam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay

val Verde = Color(0xFF2D6A4F)
val VerdeDark = Color(0xFF1A3A20)
val VerdeLight = Color(0xFFE8F5E9)
val FondoApp = Color(0xFFF4F9F5)
val AmberLight = Color(0xFFFFFBEB)
val AmberDark = Color(0xFFB45309)
val TextoPrimario = Color(0xFF1A1A1A)
val TextoSecundario = Color(0xFF6B7280)

enum class Pantalla {
    HOME,
    DETALLE
}

fun obtenerBandera(origen: String): String {
    val o = origen.lowercase()

    return when {
        o.contains("mexico") || o.contains("méxico") -> "🇲🇽"
        o.contains("europa") || o.contains("europe") -> "🇪🇺"
        o.contains("asia") -> "🌏"
        o.contains("africa") || o.contains("áfrica") -> "🌍"
        o.contains("america") || o.contains("américa") -> "🌎"
        o.contains("mediterraneo") || o.contains("mediterráneo") -> "🇬🇷"
        o.contains("brasil") || o.contains("brazil") -> "🇧🇷"
        o.contains("china") -> "🇨🇳"
        o.contains("india") -> "🇮🇳"
        o.contains("peru") || o.contains("perú") -> "🇵🇪"
        o.contains("colombia") -> "🇨🇴"
        o.contains("argentina") -> "🇦🇷"
        o.contains("estados unidos") -> "🇺🇸"
        o.contains("japon") || o.contains("japón") -> "🇯🇵"
        o.contains("australia") -> "🇦🇺"
        else -> "🌐"
    }
}

fun obtenerEmojiFormaVida(formaDeVida: String): String {
    val f = formaDeVida.lowercase()

    return when {
        f.contains("hierba") -> "🌿"
        f.contains("arbol") || f.contains("árbol") -> "🌳"
        f.contains("arbusto") -> "🌱"
        f.contains("bejuco") -> "🌾"
        f.contains("palma") -> "🌴"
        f.contains("epifita") || f.contains("epífita") -> "🪴"
        else -> "🌿"
    }
}

fun obtenerImagenPlanta(planta: Planta): Int {
    val nombreComun = planta.nombreComun.trim().lowercase()
    val nombreCientifico = planta.nombreCientifico.trim().lowercase()

    return when {
        nombreComun.contains("aloe") ||
                nombreComun.contains("sabila") ||
                nombreComun.contains("sábila") ||
                nombreCientifico.contains("aloe") -> R.drawable.aloe_vera

        nombreComun.contains("arnica") ||
                nombreComun.contains("árnica") ||
                nombreCientifico.contains("arnica") ||
                nombreCientifico.contains("árnica") -> R.drawable.arnica

        nombreComun.contains("echinacea") ||
                nombreComun.contains("equinacea") ||
                nombreCientifico.contains("echinacea") ||
                nombreCientifico.contains("equinacea") -> R.drawable.echinacea

        nombreComun.contains("jengibre") ||
                nombreCientifico.contains("zingiber") -> R.drawable.jengibre

        nombreComun.contains("lavanda") ||
                nombreCientifico.contains("lavandula") -> R.drawable.lavanda

        nombreComun.contains("manzanilla") ||
                nombreCientifico.contains("matricaria") ||
                nombreCientifico.contains("chamomilla") -> R.drawable.manzanilla

        nombreComun.contains("menta") ||
                nombreCientifico.contains("mentha") -> R.drawable.menta

        nombreComun.contains("romero") ||
                nombreCientifico.contains("rosmarinus") ||
                nombreCientifico.contains("salvia rosmarinus") -> R.drawable.romero

        nombreComun.contains("tomillo") ||
                nombreCientifico.contains("thymus") -> R.drawable.tomillo

        nombreComun.contains("valeriana") ||
                nombreCientifico.contains("valeriana") -> R.drawable.valeriana

        else -> R.drawable.manzanilla
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                AppPlantasMedicinales()
            }
        }
    }
}

@Composable
fun AppPlantasMedicinales(
    viewModel: PlantasViewModel = viewModel()
) {
    var pantallaActual by remember { mutableStateOf(Pantalla.HOME) }
    var plantaSeleccionada by remember { mutableStateOf<Planta?>(null) }
    var mostrandoCarga by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(2000)
        viewModel.cargarPlantasIniciales()
        mostrandoCarga = false
    }

    if (mostrandoCarga) {
        PantallaCargaInicial()
    } else {
        when (pantallaActual) {
            Pantalla.HOME -> {
                PantallaHome(
                    viewModel = viewModel,
                    onVerDetalle = { planta ->
                        plantaSeleccionada = planta
                        pantallaActual = Pantalla.DETALLE
                    }
                )
            }

            Pantalla.DETALLE -> {
                plantaSeleccionada?.let { planta ->
                    PantallaDetalle(
                        planta = planta,
                        onAtras = {
                            pantallaActual = Pantalla.HOME
                        }
                    )
                }
            }
        }
    }
}
@Composable
fun PantallaCargaInicial() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE8F5E9),
                        Color(0xFFB7E4C7),
                        Verde
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "🌿",
                fontSize = 58.sp
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Plantas Medicinales",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeDark
            )

            Text(
                text = "Cargando datos naturales...",
                fontSize = 14.sp,
                color = Color(0xFF374151),
                modifier = Modifier.padding(top = 6.dp)
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.manzanilla),
                    contentDescription = "Manzanilla",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                )

                Image(
                    painter = painterResource(id = R.drawable.lavanda),
                    contentDescription = "Lavanda",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(82.dp)
                        .clip(CircleShape)
                )

                Image(
                    painter = painterResource(id = R.drawable.jengibre),
                    contentDescription = "Jengibre",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.aloe_vera),
                contentDescription = "Aloe Vera",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(95.dp)
                    .clip(RoundedCornerShape(24.dp))
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            CircularProgressIndicator(
                color = VerdeDark,
                strokeWidth = 4.dp
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Preparando información de las plantas...",
                fontSize = 13.sp,
                color = VerdeDark
            )
        }
    }
}
@Composable
fun PantallaHome(
    viewModel: PlantasViewModel,
    onVerDetalle: (Planta) -> Unit
) {
    val categorias = listOf(
        "Todas",
        "Cultivada",
        "Silvestre",
        "Hierba",
        "Árbol",
        "Arbusto"
    )

    val emojis = mapOf(
        "Todas" to "🌼",
        "Cultivada" to "🌱",
        "Silvestre" to "🌿",
        "Hierba" to "🍃",
        "Árbol" to "🌳",
        "Arbusto" to "🪴"
    )

    val plantasMostradas = viewModel.plantasFiltradas

    Scaffold(
        containerColor = FondoApp
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(VerdeDark, Verde)
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Column {
                    Text(
                        text = "🌿 Plantas Medicinales",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = "Descubre el poder de la naturaleza",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            OutlinedTextField(
                value = viewModel.busqueda,
                onValueChange = { texto ->
                    viewModel.onBusquedaChange(texto)
                },
                placeholder = {
                    Text(
                        text = "Buscar planta, familia, origen...",
                        color = TextoSecundario
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null,
                        tint = Verde
                    )
                },
                trailingIcon = {
                    if (viewModel.busqueda.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                viewModel.onBusquedaChange("")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = "Limpiar",
                                tint = Verde
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Verde,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                singleLine = true
            )

            Text(
                text = "Filtrar por tipo",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextoPrimario,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                items(categorias) { categoria ->
                    val activa = categoria == viewModel.categoriaActiva

                    FilterChip(
                        selected = activa,
                        onClick = {
                            viewModel.cargarPorCategoria(categoria)
                        },
                        label = {
                            Text(
                                text = "${emojis[categoria]} $categoria",
                                fontWeight = if (activa) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Verde,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = Verde
                        )
                    )
                }
            }

            when {
                viewModel.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = Verde
                            )

                            Spacer(
                                modifier = Modifier.height(16.dp)
                            )

                            Text(
                                text = "Buscando plantas...",
                                color = TextoSecundario
                            )
                        }
                    }
                }

                viewModel.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "❌",
                                fontSize = 48.sp
                            )

                            Text(
                                text = viewModel.error ?: "",
                                color = TextoSecundario,
                                modifier = Modifier.padding(16.dp)
                            )

                            Button(
                                onClick = {
                                    viewModel.cargarPorCategoria(viewModel.categoriaActiva)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Verde
                                )
                            ) {
                                Text(
                                    text = "Reintentar"
                                )
                            }
                        }
                    }
                }

                else -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (viewModel.busqueda.isEmpty()) {
                                "${plantasMostradas.size} plantas"
                            } else {
                                "${plantasMostradas.size} resultados para \"${viewModel.busqueda}\""
                            },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoSecundario
                        )
                    }

                    if (plantasMostradas.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "🌿",
                                    fontSize = 64.sp
                                )

                                Spacer(
                                    modifier = Modifier.height(16.dp)
                                )

                                Text(
                                    text = "No encontramos plantas",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextoPrimario
                                )

                                Text(
                                    text = "Intenta con otro filtro o búsqueda",
                                    fontSize = 14.sp,
                                    color = TextoSecundario
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(
                                horizontal = 16.dp,
                                vertical = 4.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(plantasMostradas) { planta ->
                                TarjetaPlanta(
                                    planta = planta,
                                    onClick = {
                                        onVerDetalle(planta)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PantallaDetalle(
    planta: Planta,
    onAtras: () -> Unit
) {
    val scrollState = rememberScrollState()
    val bandera = obtenerBandera(planta.origen)
    val emojiVida = obtenerEmojiFormaVida(planta.formaDeVida)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoApp)
    ) {
        Column(
            modifier = Modifier.verticalScroll(scrollState)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                AsyncImage(
                    model = obtenerImagenPlanta(planta),
                    contentDescription = planta.nombreComun,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.2f),
                                    Color.Black.copy(alpha = 0.6f)
                                ),
                                startY = 100f
                            )
                        )
                )

                IconButton(
                    onClick = onAtras,
                    modifier = Modifier
                        .padding(12.dp)
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.9f), CircleShape)
                        .align(Alignment.TopStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = Verde
                    )
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = planta.nombreComun,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = bandera,
                            fontSize = 24.sp
                        )
                    }

                    Text(
                        text = planta.nombreCientifico,
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        fontStyle = FontStyle.Italic
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-20).dp),
                shape = RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 0.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AssistChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = "🌱 ${planta.familia.take(14)}",
                                    fontSize = 11.sp,
                                    color = Verde
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = VerdeLight
                            ),
                            border = BorderStroke(0.dp, Color.Transparent),
                            modifier = Modifier.weight(1f)
                        )

                        AssistChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = "$emojiVida ${planta.formaDeVida.take(10)}",
                                    fontSize = 11.sp,
                                    color = Verde
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = VerdeLight
                            ),
                            border = BorderStroke(0.dp, Color.Transparent),
                            modifier = Modifier.weight(1f)
                        )

                        AssistChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = if (planta.manejo.contains("cultivada", ignoreCase = true)) {
                                        "🌾 Cultivada"
                                    } else {
                                        "🌿 Silvestre"
                                    },
                                    fontSize = 11.sp,
                                    color = Verde
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = VerdeLight
                            ),
                            border = BorderStroke(0.dp, Color.Transparent),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = AmberLight
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 0.dp
                        ),
                        border = BorderStroke(1.dp, Color(0xFFFCD34D))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = bandera,
                                fontSize = 36.sp
                            )

                            Spacer(
                                modifier = Modifier.width(12.dp)
                            )

                            Column {
                                Text(
                                    text = "Origen",
                                    fontSize = 11.sp,
                                    color = TextoSecundario,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = planta.origen,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AmberDark
                                )
                            }
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    if (planta.habitat.isNotEmpty()) {
                        Text(
                            text = "🏕️  HÁBITAT",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = VerdeDark,
                            letterSpacing = 0.8.sp
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = VerdeLight
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 0.dp
                            )
                        ) {
                            Text(
                                text = planta.habitat,
                                fontSize = 13.sp,
                                color = VerdeDark,
                                lineHeight = 20.sp,
                                modifier = Modifier.padding(14.dp)
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )
                    }

                    Text(
                        text = "📋  DESCRIPCIÓN Y USO",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdeDark,
                        letterSpacing = 0.8.sp
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFEDF7F0)
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 0.dp
                        )
                    ) {
                        Text(
                            text = planta.descripcionUso,
                            fontSize = 14.sp,
                            color = Color(0xFF374151),
                            lineHeight = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TarjetaPlanta(
    planta: Planta,
    onClick: () -> Unit
) {
    val bandera = obtenerBandera(planta.origen)
    val emojiVida = obtenerEmojiFormaVida(planta.formaDeVida)

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = obtenerImagenPlanta(planta),
                contentDescription = planta.nombreComun,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = planta.nombreComun,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoPrimario
                    )

                    Text(
                        text = bandera,
                        fontSize = 13.sp
                    )
                }

                Text(
                    text = planta.nombreCientifico,
                    fontSize = 11.sp,
                    color = TextoSecundario,
                    fontStyle = FontStyle.Italic
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    AssistChip(
                        onClick = {},
                        label = {
                            Text(
                                text = "$emojiVida ${planta.formaDeVida.take(8)}",
                                fontSize = 9.sp
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = VerdeLight,
                            labelColor = Verde
                        ),
                        border = BorderStroke(0.dp, Color.Transparent),
                        modifier = Modifier.height(22.dp)
                    )

                    AssistChip(
                        onClick = {},
                        label = {
                            Text(
                                text = planta.familia.take(10),
                                fontSize = 9.sp
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color(0xFFF0F4F0),
                            labelColor = TextoSecundario
                        ),
                        border = BorderStroke(0.dp, Color.Transparent),
                        modifier = Modifier.height(22.dp)
                    )
                }

                Text(
                    text = planta.descripcionUso,
                    fontSize = 11.sp,
                    color = TextoSecundario,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFF9DB5A0),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}