package com.example.plantas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantas.ui.theme.PlantasDeMamáTheme
import com.example.plantasdemam.Actividad2
import com.example.plantasdemam.Planta
import com.example.plantasdemam.plantasData

val Verde = Color(0xFF2D6A4F)
val VerdeDark = Color(0xFF1A3A20)
val VerdeLight = Color(0xFFE8F5E9)
val FondoApp = Color(0xFFF4F9F5)
val Amber = Color(0xFFF5A623)
val AmberLight = Color(0xFFFFFBEB)
val AmberDark = Color(0xFFB45309)
val TextoPrimario = Color(0xFF1A1A1A)
val TextoSecundario = Color(0xFF6B7280)

enum class Pantalla { HOME, BUSCAR, DETALLE, FAVORITOS }

@Composable
fun imagenDePlanta(nombre: String): Int {
    val context = LocalContext.current
    return context.resources.getIdentifier(nombre, "drawable", context.packageName)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlantasDeMamáTheme {
                AppPlantasMedicinales()
            }
        }
    }
}

@Composable
fun AppPlantasMedicinales() {
    var pantallaActual by remember { mutableStateOf(Pantalla.HOME) }
    var plantaSeleccionada by remember { mutableStateOf<Planta?>(null) }
    var plantas by remember { mutableStateOf(plantasData) }

    when (pantallaActual) {
        Pantalla.HOME -> PantallaHome(
            plantas = plantas,
            onVerDetalle = { planta -> plantaSeleccionada = planta; pantallaActual = Pantalla.DETALLE },
            onIrFavoritos = { pantallaActual = Pantalla.FAVORITOS },
            onIrBuscar = { pantallaActual = Pantalla.BUSCAR }
        )
        Pantalla.BUSCAR -> PantallaBuscar(
            plantas = plantas,
            onVerDetalle = { planta -> plantaSeleccionada = planta; pantallaActual = Pantalla.DETALLE },
            onAtras = { pantallaActual = Pantalla.HOME },
            onIrFavoritos = { pantallaActual = Pantalla.FAVORITOS }
        )
        Pantalla.DETALLE -> plantaSeleccionada?.let { planta ->
            PantallaDetalle(
                planta = planta,
                onAtras = { pantallaActual = Pantalla.HOME },
                onToggleFavorito = { p ->
                    plantas = plantas.map { if (it.id == p.id) it.copy(esFavorita = !it.esFavorita) else it }
                    plantaSeleccionada = p.copy(esFavorita = !p.esFavorita)
                }
            )
        }
        Pantalla.FAVORITOS -> PantallaFavoritos(
            plantas = plantas.filter { it.esFavorita },
            onVerDetalle = { planta -> plantaSeleccionada = planta; pantallaActual = Pantalla.DETALLE },
            onAtras = { pantallaActual = Pantalla.HOME }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaHome(
    plantas: List<Planta>,
    onVerDetalle: (Planta) -> Unit,
    onIrFavoritos: () -> Unit,
    onIrBuscar: () -> Unit
) {
    var busqueda by remember { mutableStateOf("") }
    var categoriaActiva by remember { mutableStateOf("Todas") }
    val categorias = listOf("Todas", "Digestivo", "Relajante", "Inmune", "Antiinflamatorio")
    val emojis = mapOf("Todas" to "🌼", "Digestivo" to "🍃", "Relajante" to "😴", "Inmune" to "🛡️", "Antiinflamatorio" to "🔥")

    val plantasFiltradas = plantas.filter { planta ->
        val matchBusqueda = busqueda.isEmpty() ||
                planta.nombreComun.contains(busqueda, ignoreCase = true) ||
                planta.nombreCientifico.contains(busqueda, ignoreCase = true) ||
                planta.beneficios.any { it.contains(busqueda, ignoreCase = true) }
        val matchCategoria = categoriaActiva == "Todas" || planta.categoria.equals(categoriaActiva, ignoreCase = true)
        matchBusqueda && matchCategoria
    }

    Scaffold(
        containerColor = FondoApp,
        bottomBar = {
            BottomNavBar(pantallaActual = Pantalla.HOME, onHome = {}, onBuscar = onIrBuscar, onFavoritos = onIrFavoritos)
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🌿 Plantas de Mamá", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = VerdeDark)
                IconButton(onClick = onIrFavoritos, modifier = Modifier.size(40.dp).background(Color.White, CircleShape)) {
                    Icon(imageVector = Icons.Filled.Favorite, contentDescription = "Favoritos", tint = Verde)
                }
            }
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                placeholder = { Text("Buscar planta o beneficio...", color = TextoSecundario) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Verde) },
                trailingIcon = {
                    if (busqueda.isNotEmpty()) {
                        IconButton(onClick = { busqueda = "" }) { Icon(Icons.Filled.Clear, contentDescription = "Limpiar", tint = Verde) }
                    }
                },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Verde, unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Categorías", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextoPrimario, modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 8.dp)) {
                items(categorias) { categoria ->
                    val activa = categoria == categoriaActiva
                    FilterChip(
                        selected = activa, onClick = { categoriaActiva = categoria },
                        label = { Text("${emojis[categoria]} $categoria", fontWeight = if (activa) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Verde, selectedLabelColor = Color.White, containerColor = Color.White, labelColor = Verde)
                    )
                }
            }
            Text(
                text = if (busqueda.isEmpty()) "Plantas Populares" else "${plantasFiltradas.size} resultados para \"$busqueda\"",
                fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextoPrimario,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(plantasFiltradas) { planta -> TarjetaPlanta(planta = planta, onClick = { onVerDetalle(planta) }) }
            }
        }
    }
}

@Composable
fun PantallaBuscar(plantas: List<Planta>, onVerDetalle: (Planta) -> Unit, onAtras: () -> Unit, onIrFavoritos: () -> Unit) {
    var busqueda by remember { mutableStateOf("") }
    val sugerencias = listOf("Digestión", "Sueño", "Energía", "Ansiedad", "Gripe")
    val resultados = if (busqueda.isEmpty()) emptyList()
    else plantas.filter { planta ->
        planta.nombreComun.contains(busqueda, ignoreCase = true) ||
                planta.nombreCientifico.contains(busqueda, ignoreCase = true) ||
                planta.beneficios.any { it.contains(busqueda, ignoreCase = true) } ||
                planta.categoria.contains(busqueda, ignoreCase = true)
    }
    Scaffold(
        containerColor = FondoApp,
        bottomBar = { BottomNavBar(pantallaActual = Pantalla.BUSCAR, onHome = onAtras, onBuscar = {}, onFavoritos = onIrFavoritos) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onAtras) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Verde) }
                OutlinedTextField(
                    value = busqueda, onValueChange = { busqueda = it },
                    placeholder = { Text("Buscar planta o beneficio...", color = TextoSecundario) },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Verde) },
                    trailingIcon = { if (busqueda.isNotEmpty()) { IconButton(onClick = { busqueda = "" }) { Icon(Icons.Filled.Clear, contentDescription = "Limpiar", tint = Verde) } } },
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Verde, unfocusedBorderColor = VerdeLight, focusedContainerColor = Color.White, unfocusedContainerColor = Color.White),
                    modifier = Modifier.weight(1f), singleLine = true
                )
            }
            if (busqueda.isEmpty()) {
                Text("¿No encontraste lo que buscas? Intenta con el beneficio", fontSize = 14.sp, color = TextoSecundario, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(sugerencias) { sugerencia ->
                        SuggestionChip(onClick = { busqueda = sugerencia }, label = { Text(sugerencia, color = Verde, fontWeight = FontWeight.SemiBold) }, colors = SuggestionChipDefaults.suggestionChipColors(containerColor = VerdeLight))
                    }
                }
            } else if (resultados.isEmpty()) {
                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text("🌿", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No encontramos ninguna planta", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextoPrimario)
                    Text("Intenta con otro nombre o beneficio", fontSize = 14.sp, color = TextoSecundario, modifier = Modifier.padding(top = 8.dp))
                    OutlinedButton(onClick = { busqueda = "" }, modifier = Modifier.padding(top = 16.dp), colors = ButtonDefaults.outlinedButtonColors(contentColor = Verde), border = BorderStroke(1.dp, Verde)) {
                        Text("Ver todas las plantas")
                    }
                }
            } else {
                Text("${resultados.size} resultados para \"$busqueda\"", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextoPrimario, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(resultados) { planta -> TarjetaPlantaBusqueda(planta = planta, onClick = { onVerDetalle(planta) }) }
                }
            }
        }
    }
}

@Composable
fun PantallaDetalle(planta: Planta, onAtras: () -> Unit, onToggleFavorito: (Planta) -> Unit) {
    val scrollState = rememberScrollState()
    val imagenId = imagenDePlanta(planta.imagenNombre)
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize().background(FondoApp)) {
        Column(modifier = Modifier.verticalScroll(scrollState)) {
            Box(modifier = Modifier.fillMaxWidth().height(220.dp)) {
                Image(painter = painterResource(id = imagenId), contentDescription = planta.nombreComun, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.3f)), startY = 300f)))
                IconButton(onClick = onAtras, modifier = Modifier.padding(12.dp).size(40.dp).background(Color.White, CircleShape).align(Alignment.TopStart)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Verde)
                }
                IconButton(onClick = { onToggleFavorito(planta) }, modifier = Modifier.padding(12.dp).size(40.dp).background(Color.White, CircleShape).align(Alignment.TopEnd)) {
                    Icon(imageVector = if (planta.esFavorita) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder, contentDescription = "Favorito", tint = Verde)
                }
            }
            Card(modifier = Modifier.fillMaxWidth().offset(y = (-20).dp), shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(0.dp)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(planta.nombreComun, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Verde)
                    Text(planta.nombreCientifico, fontSize = 13.sp, color = TextoSecundario, fontStyle = FontStyle.Italic)
                    AssistChip(onClick = {}, label = { Text("🌿 ${planta.categoria}", color = Verde, fontWeight = FontWeight.SemiBold) }, modifier = Modifier.padding(top = 10.dp), colors = AssistChipDefaults.assistChipColors(containerColor = VerdeLight), border = BorderStroke(1.dp, Verde))
                    Spacer(Modifier.height(20.dp))
                    Text("🍃  BENEFICIOS", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = VerdeDark, letterSpacing = 0.8.sp)
                    Spacer(Modifier.height(8.dp))
                    planta.beneficios.forEach { beneficio ->
                        Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.Top) {
                            Text("✅ ", fontSize = 14.sp)
                            Text(beneficio, fontSize = 14.sp, color = TextoPrimario, lineHeight = 20.sp)
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    Text("DESCRIPCIÓN", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = VerdeDark, letterSpacing = 0.8.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(planta.descripcion, fontSize = 14.sp, color = Color(0xFF374151), lineHeight = 22.sp)
                    Spacer(Modifier.height(20.dp))
                    Text("☕  CÓMO USARLA", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = VerdeDark, letterSpacing = 0.8.sp)
                    Spacer(Modifier.height(8.dp))
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFEDF7F0)), elevation = CardDefaults.cardElevation(0.dp)) {
                        Text(planta.formaDeUso, fontSize = 13.sp, color = Verde, lineHeight = 20.sp, modifier = Modifier.padding(16.dp))
                    }
                    Spacer(Modifier.height(20.dp))
                    Text("⚠️  ADVERTENCIA", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Amber, letterSpacing = 0.8.sp)
                    Spacer(Modifier.height(8.dp))
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = AmberLight), elevation = CardDefaults.cardElevation(0.dp), border = BorderStroke(1.dp, Color(0xFFFCD34D))) {
                        Text(planta.contraindicaciones, fontSize = 13.sp, color = AmberDark, lineHeight = 20.sp, modifier = Modifier.padding(16.dp))
                    }
                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = { onToggleFavorito(planta) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Verde)
                    ) {
                        Text(text = if (planta.esFavorita) "❤️ Guardado en favoritos" else "🤍 Guardar en favoritos", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = {
                            val intent = android.content.Intent(context, Actividad2::class.java)
                            intent.putExtra("videoId", planta.videoId)
                            intent.putExtra("titulo", planta.nombreComun)
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A3A20))
                    ) {
                        Text(text = "▶️ Ver video", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun PantallaFavoritos(plantas: List<Planta>, onVerDetalle: (Planta) -> Unit, onAtras: () -> Unit) {
    Scaffold(containerColor = FondoApp) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onAtras) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Verde) }
                Text("❤️ Mis Favoritos", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = VerdeDark)
            }
            if (plantas.isEmpty()) {
                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text("🌿", fontSize = 64.sp)
                    Text("No tienes favoritos aún", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextoPrimario, modifier = Modifier.padding(top = 16.dp))
                    Text("Guarda plantas para verlas aquí", fontSize = 14.sp, color = TextoSecundario, modifier = Modifier.padding(top = 8.dp))
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(plantas) { planta -> TarjetaPlanta(planta = planta, onClick = { onVerDetalle(planta) }) }
                }
            }
        }
    }
}

@Composable
fun TarjetaPlanta(planta: Planta, onClick: () -> Unit) {
    val imagenId = imagenDePlanta(planta.imagenNombre)
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = imagenId), contentDescription = planta.nombreComun, contentScale = ContentScale.Crop, modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)))
            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Text(planta.nombreComun, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextoPrimario)
                Text(planta.nombreCientifico, fontSize = 12.sp, color = TextoSecundario, fontStyle = FontStyle.Italic)
                Row(modifier = Modifier.padding(top = 6.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    AssistChip(onClick = {}, label = { Text(planta.categoria, fontSize = 10.sp, fontWeight = FontWeight.Bold) }, colors = AssistChipDefaults.assistChipColors(containerColor = VerdeLight, labelColor = Verde), border = BorderStroke(0.dp, Color.Transparent), modifier = Modifier.height(24.dp))
                    if (planta.beneficios.isNotEmpty()) {
                        AssistChip(onClick = {}, label = { Text(planta.beneficios[0].take(15), fontSize = 10.sp, fontStyle = FontStyle.Italic) }, colors = AssistChipDefaults.assistChipColors(containerColor = Color(0xFFF5F5F5), labelColor = TextoSecundario), border = BorderStroke(0.dp, Color.Transparent), modifier = Modifier.height(24.dp))
                    }
                }
            }
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color(0xFF9DB5A0), modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun TarjetaPlantaBusqueda(planta: Planta, onClick: () -> Unit) {
    val imagenId = imagenDePlanta(planta.imagenNombre)
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = imagenId), contentDescription = planta.nombreComun, contentScale = ContentScale.Crop, modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)))
            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Text(planta.nombreComun, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextoPrimario)
                Text(text = planta.descripcion, fontSize = 13.sp, color = TextoSecundario, maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 2.dp))
            }
            Icon(Icons.Filled.Search, contentDescription = null, tint = Verde, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun BottomNavBar(pantallaActual: Pantalla, onHome: () -> Unit, onBuscar: () -> Unit, onFavoritos: () -> Unit) {
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        NavigationBarItem(selected = pantallaActual == Pantalla.HOME, onClick = onHome, icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") }, label = { Text("Inicio", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = Verde, selectedTextColor = Verde, indicatorColor = VerdeLight, unselectedIconColor = TextoSecundario, unselectedTextColor = TextoSecundario))
        NavigationBarItem(selected = pantallaActual == Pantalla.BUSCAR, onClick = onBuscar, icon = { Icon(Icons.Filled.Search, contentDescription = "Buscar") }, label = { Text("Buscar", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = Verde, selectedTextColor = Verde, indicatorColor = VerdeLight, unselectedIconColor = TextoSecundario, unselectedTextColor = TextoSecundario))
        NavigationBarItem(selected = pantallaActual == Pantalla.FAVORITOS, onClick = onFavoritos, icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favoritos") }, label = { Text("Favoritos", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = Verde, selectedTextColor = Verde, indicatorColor = VerdeLight, unselectedIconColor = TextoSecundario, unselectedTextColor = TextoSecundario))
    }
}