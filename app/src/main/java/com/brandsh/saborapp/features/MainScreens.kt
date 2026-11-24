package com.brandsh.saborapp.features

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.brandsh.saborapp.data.MockData
import com.brandsh.saborapp.designsystem.LocalSaborPalette
import com.brandsh.saborapp.designsystem.SaborAddButton
import com.brandsh.saborapp.designsystem.SaborCard
import com.brandsh.saborapp.designsystem.SaborCanvas
import com.brandsh.saborapp.designsystem.SaborChip
import com.brandsh.saborapp.designsystem.SaborIcon
import com.brandsh.saborapp.designsystem.SaborImage
import com.brandsh.saborapp.designsystem.SaborNavRow
import com.brandsh.saborapp.designsystem.SaborPrimaryButton
import com.brandsh.saborapp.designsystem.SaborRating
import com.brandsh.saborapp.designsystem.SaborSectionHeader
import com.brandsh.saborapp.designsystem.SaborSpacing
import com.brandsh.saborapp.designsystem.SaborTag
import com.brandsh.saborapp.designsystem.SaborTextField
import com.brandsh.saborapp.designsystem.SaborToggleRow
import com.brandsh.saborapp.designsystem.SaborTopBar
import com.brandsh.saborapp.models.AppTab
import com.brandsh.saborapp.models.Dish
import com.brandsh.saborapp.models.OrderStatus
import com.brandsh.saborapp.stores.AppState
import com.brandsh.saborapp.stores.AppUiState

@Composable
fun SaborScrollableScreen(
    modifier: Modifier = Modifier,
    horizontalPadding: androidx.compose.ui.unit.Dp = SaborSpacing.margin,
    bottomPadding: androidx.compose.ui.unit.Dp = 24.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(modifier.fillMaxSize()) {
        SaborCanvas()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = horizontalPadding)
                .padding(top = SaborSpacing.sm, bottom = bottomPadding),
            verticalArrangement = Arrangement.spacedBy(SaborSpacing.lg),
            content = content,
        )
    }
}

@Composable
fun OnboardingScreen(
    onSkip: () -> Unit,
    onSignIn: () -> Unit,
    onRegister: () -> Unit,
) {
    data class Slide(val image: String, val title: String, val description: String, val cta: String)
    val slides = remember {
        listOf(
            Slide(
                "Hamburguesa Trufada Sabor",
                "Descubre nuevos sabores artesanales",
                "Platos de alta cocina preparados al momento con ingredientes frescos de origen local directamente a tu mesa.",
                "Comenzar a ordenar",
            ),
            Slide(
                "Raviolis de Boletus y Foie",
                "Recetas de autor y sazón auténtica",
                "Creadas por maestros culinarios que transforman cada bocado en una experiencia sensorial inolvidable.",
                "Explorar menú",
            ),
            Slide(
                "Salmón Glaseado al Miso",
                "La mesa servida donde prefieras",
                "Servicio impecable con empaques ecológicos diseñados para conservar la textura y temperatura ideal.",
                "Crear mi cuenta",
            ),
        )
    }
    var index by remember { mutableIntStateOf(0) }
    val slide = slides[index]

    Box(Modifier.fillMaxSize()) {
        SaborCanvas()
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Box(Modifier.fillMaxWidth().height(390.dp)) {
                SaborImage(slide.image, Modifier.fillMaxSize())
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                    androidx.compose.ui.graphics.Color.Transparent,
                                    MaterialTheme.colorScheme.background,
                                ),
                            ),
                        ),
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(horizontal = SaborSpacing.margin, vertical = 48.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.surface.copy(alpha = 0.86f)) {
                        Text(
                            "🔥  Sabor",
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                    TextButton(onClick = onSkip) { Text("Saltar  ›") }
                }
            }

            Column(
                Modifier.padding(SaborSpacing.margin),
                verticalArrangement = Arrangement.spacedBy(SaborSpacing.md),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SaborTag("Ingredientes 100% de origen local", icon = "leaf.fill")
                    Spacer(Modifier.weight(1f))
                    SaborRating(4.9)
                }
                Text(slide.title, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
                Text(
                    slide.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(SaborSpacing.lg)) {
                    FeaturePill("timer", "Entrega ágil", "En su punto exacto")
                    FeaturePill("fork.knife", "Menú de Autor", "Chefs destacados")
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    slides.indices.forEach { slideIndex ->
                        Box(
                            Modifier
                                .width(if (slideIndex == index) 22.dp else 8.dp)
                                .height(8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (slideIndex == index) MaterialTheme.colorScheme.primary
                                    else LocalSaborPalette.current.containerHighest,
                                ),
                        )
                    }
                }
                SaborPrimaryButton(
                    title = slide.cta,
                    onClick = {
                        if (index < slides.lastIndex) index++ else onRegister()
                    },
                )
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("¿Ya tienes una cuenta? ", style = MaterialTheme.typography.bodySmall)
                    TextButton(onClick = onSignIn) { Text("Iniciar sesión") }
                }
            }
        }
    }
}

@Composable
private fun FeaturePill(icon: String, title: String, subtitle: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)) {
            Box(Modifier.size(32.dp), contentAlignment = Alignment.Center) {
                SaborIcon(icon, Modifier.size(16.dp), MaterialTheme.colorScheme.primary)
            }
        }
        Column {
            Text(title, style = MaterialTheme.typography.labelMedium)
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun SignInScreen(
    onBack: () -> Unit,
    onRegister: () -> Unit,
    onSignedIn: () -> Unit,
) {
    var email by remember { mutableStateOf("valentina@correo.com") }
    var password by remember { mutableStateOf("sabor1234") }
    var showPassword by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }

    SaborScrollableScreen(bottomPadding = 44.dp) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(onClick = onBack) { Text("‹  Volver") }
            TextButton(onClick = onRegister) { Text("Crear cuenta") }
        }
        Text("🔥  Sabor", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        SaborTag("ALTA COCINA A TU MESA", icon = "fork.knife")
        Text("¡Bienvenido de vuelta!", style = MaterialTheme.typography.headlineLarge)
        Text(
            "Inicia sesión para saborear tus platos favoritos y disfrutar de beneficios exclusivos.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        SaborTag("Correo verificado", icon = "check.circle")
        SaborTextField(
            label = "Correo electrónico",
            placeholder = "ejemplo@correo.com",
            value = email,
            onValueChange = { email = it },
            icon = "email",
            keyboardType = KeyboardType.Email,
        )
        SaborTextField(
            label = "Contraseña",
            placeholder = "••••••••",
            value = password,
            onValueChange = { password = it },
            icon = "lock.fill",
            isSecure = !showPassword,
            trailingIcon = if (showPassword) "visibility.off" else "visibility",
            onTrailingClick = { showPassword = !showPassword },
        )
        TextButton(onClick = {}) { Text("¿Olvidaste tu contraseña?") }
        Row(
            Modifier
                .fillMaxWidth()
                .clickable { rememberMe = !rememberMe },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            androidx.compose.material3.Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it })
            Text("Recordarme en este dispositivo")
        }
        SaborPrimaryButton("Iniciar sesión", onClick = onSignedIn)
        CenteredDivider("o continúa con")
        Row(horizontalArrangement = Arrangement.spacedBy(SaborSpacing.sm)) {
            OutlinedButton(onClick = onSignedIn, modifier = Modifier.weight(1f)) { Text("Google") }
            OutlinedButton(onClick = onSignedIn, modifier = Modifier.weight(1f)) { Text("Apple") }
        }
        TextButton(onClick = onSignedIn, modifier = Modifier.fillMaxWidth()) {
            Text("Explorar menú como invitado")
        }
        Text(
            "🔒  Tus datos están protegidos con cifrado de grado bancario",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onSignIn: () -> Unit,
    onRegistered: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var acceptedTerms by remember { mutableStateOf(false) }
    var offers by remember { mutableStateOf(true) }
    val passwordStrength = listOf(
        password.length >= 8,
        password.any(Char::isLowerCase) && password.any(Char::isUpperCase),
        password.any(Char::isDigit),
        password.any { !it.isLetterOrDigit() },
    ).count { it }
    val strengthLabel = when (passwordStrength) {
        0 -> "Añade una contraseña"
        1 -> "Muy débil"
        2 -> "Débil"
        3 -> "Fuerte"
        else -> "Muy fuerte"
    }
    val strengthColor = when {
        passwordStrength <= 1 -> MaterialTheme.colorScheme.error
        passwordStrength == 2 -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.secondary
    }

    SaborScrollableScreen(bottomPadding = 44.dp) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(onClick = onBack) { Text("‹  Volver") }
            Text("Paso 1 de 2: Datos de cuenta", style = MaterialTheme.typography.labelSmall)
        }
        Text("Crea tu cuenta Sabor", style = MaterialTheme.typography.headlineLarge)
        Text(
            "Únete al club gastronómico y recibe 10 € de bienvenida en tu primer pedido artesanal.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        SaborTag("10 € de regalo para nuevos miembros", icon = "gift")
        SaborTextField("Nombre", name, { name = it }, placeholder = "Valentina", icon = "person.fill")
        SaborTextField("Apellidos", surname, { surname = it }, placeholder = "González")
        SaborTextField(
            "Correo electrónico",
            email,
            { email = it },
            placeholder = "valentina@correo.com",
            icon = "email",
            keyboardType = KeyboardType.Email,
        )
        SaborTextField("Teléfono móvil", phone, { phone = it }, placeholder = "🇪🇸 +34 612 345 678", icon = "phone")
        SaborTextField(
            "Contraseña",
            password,
            { password = it },
            placeholder = "••••••••",
            icon = "lock.fill",
            isSecure = true,
        )
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            repeat(4) { index ->
                Box(
                    Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(
                            if (index < passwordStrength) strengthColor else LocalSaborPalette.current.sand,
                        ),
                )
            }
        }
        Text(
            strengthLabel,
            color = strengthColor,
            style = MaterialTheme.typography.labelMedium,
        )
        SaborTextField(
            "Confirmar contraseña",
            confirmPassword,
            { confirmPassword = it },
            placeholder = "••••••••",
            icon = "lock.fill",
            isSecure = true,
        )
        if (confirmPassword.isNotEmpty() && confirmPassword != password) {
            Text(
                "Las contraseñas no coinciden.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Row(verticalAlignment = Alignment.Top) {
            androidx.compose.material3.Checkbox(
                checked = acceptedTerms,
                onCheckedChange = { acceptedTerms = it },
            )
            Text(
                "Acepto los Términos del Servicio y la Política de Privacidad de Sabor.",
                modifier = Modifier.padding(top = 12.dp),
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Row(verticalAlignment = Alignment.Top) {
            androidx.compose.material3.Checkbox(checked = offers, onCheckedChange = { offers = it })
            Text(
                "Deseo recibir ofertas exclusivas, novedades del chef y promociones culinarias por email y SMS.",
                modifier = Modifier.padding(top = 12.dp),
                style = MaterialTheme.typography.bodySmall,
            )
        }
        SaborPrimaryButton(
            "Crear cuenta y comenzar",
            enabled = acceptedTerms &&
                passwordStrength >= 3 &&
                confirmPassword.isNotEmpty() &&
                confirmPassword == password,
            onClick = onRegistered,
        )
        CenteredDivider("o regístrate con")
        OutlinedButton(onClick = onRegistered, modifier = Modifier.fillMaxWidth()) { Text("Google  ·  Apple") }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text("¿Ya tienes una cuenta?", style = MaterialTheme.typography.bodySmall)
            TextButton(onClick = onSignIn) { Text("Iniciar sesión") }
        }
    }
}

@Composable
private fun CenteredDivider(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        androidx.compose.material3.HorizontalDivider(Modifier.weight(1f), color = LocalSaborPalette.current.sand)
        Text(text, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        androidx.compose.material3.HorizontalDivider(Modifier.weight(1f), color = LocalSaborPalette.current.sand)
    }
}

@Composable
fun HomeScreen(
    state: AppUiState,
    appState: AppState,
    onNavigate: (String) -> Unit,
    onSelectTab: (AppTab) -> Unit,
) {
    SaborScrollableScreen(bottomPadding = 110.dp) {
        SaborTopBar(
            location = state.addressLabel,
            onNotifications = { onNavigate("notifications") },
            onCart = { onNavigate("cart") },
            onProfile = { onSelectTab(AppTab.PROFILE) },
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("¡Hola, ${state.user.name.substringBefore(' ')}! 👋", style = MaterialTheme.typography.headlineSmall)
                Text("¿Qué se te antoja degustar hoy?", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            SaborIcon("flame", Modifier.size(28.dp), MaterialTheme.colorScheme.primary)
        }
        Surface(
            modifier = Modifier.fillMaxWidth().clickable { onSelectTab(AppTab.MENU) },
            shape = CircleShape,
            color = LocalSaborPalette.current.containerLowest,
            border = androidx.compose.foundation.BorderStroke(1.dp, LocalSaborPalette.current.sand),
        ) {
            Row(
                Modifier.padding(horizontal = 14.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SaborIcon("search", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.width(10.dp))
                Text("Buscar hamburguesas, pastas, postres...", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.weight(1f))
                SaborIcon("tune", tint = MaterialTheme.colorScheme.primary)
            }
        }
        PromoBanner()
        Column(verticalArrangement = Arrangement.spacedBy(SaborSpacing.md)) {
            SaborSectionHeader("Explorar Categorías", actionTitle = "Ver todo") { onSelectTab(AppTab.MENU) }
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(SaborSpacing.sm)) {
                MockData.categories.forEachIndexed { index, category ->
                    SaborChip(
                        title = category,
                        icon = MockData.categoryIcons.getOrNull(index),
                        selected = state.selectedCategory == category,
                        onClick = {
                            appState.updateState { it.copy(selectedCategory = category) }
                            onSelectTab(AppTab.MENU)
                        },
                    )
                }
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(SaborSpacing.md)) {
            SaborSectionHeader("Más vendidos", icon = "flame", actionTitle = "Ver carta") {
                onSelectTab(AppTab.MENU)
            }
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(SaborSpacing.md)) {
                state.dishes.filter { it.badge == "Top Ventas" || it.rating >= 4.9 }.take(5).forEach { dish ->
                    CompactDishCard(dish, state, appState, onOpen = { onNavigate("dish/${dish.id}") })
                }
            }
        }
        state.dishes.firstOrNull { it.badge == "Recomendado del Chef" }?.let { dish ->
            Column(verticalArrangement = Arrangement.spacedBy(SaborSpacing.md)) {
                SaborSectionHeader("Recomendado para ti", icon = "check.circle")
                SaborCard(Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(SaborSpacing.md), horizontalArrangement = Arrangement.spacedBy(SaborSpacing.md)) {
                        SaborImage(dish.imageKey, Modifier.size(96.dp).clip(RoundedCornerShape(16.dp)))
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            SaborTag("Recomendado del Chef")
                            Text(dish.name, style = MaterialTheme.typography.titleMedium)
                            Text(dish.description, style = MaterialTheme.typography.bodySmall, maxLines = 2)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(dish.priceText, color = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.width(8.dp))
                                SaborRating(dish.rating)
                                Spacer(Modifier.weight(1f))
                                SaborAddButton(onClick = { appState.addToCart(dish) })
                            }
                        }
                    }
                }
            }
        }
        state.dishes.firstOrNull { it.oldPrice != null }?.let { dish ->
            Column(verticalArrangement = Arrangement.spacedBy(SaborSpacing.md)) {
                SaborSectionHeader("Ofertas especiales", icon = "tag.fill", actionTitle = "Ver ofertas") {
                    onSelectTab(AppTab.MENU)
                }
                DishCardWide(dish, state, appState, onOpen = { onNavigate("dish/${dish.id}") })
            }
        }
    }
}

@Composable
private fun PromoBanner() {
    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer)))
            .padding(SaborSpacing.md),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(SaborSpacing.sm)) {
            SaborTag("EDICIÓN GOURMET", icon = "star", tint = MaterialTheme.colorScheme.onPrimary)
            Text("Festival de Sabores de Temporada", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onPrimary)
            Text("25% de descuento en platos de autor usando el código SABOR25", color = MaterialTheme.colorScheme.onPrimary)
            Text(
                "Aprovechar oferta   →",
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable { }
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Composable
private fun CompactDishCard(
    dish: Dish,
    state: AppUiState,
    appState: AppState,
    onOpen: () -> Unit,
) {
    SaborCard(Modifier.width(220.dp)) {
        Box {
            SaborImage(dish.imageKey, Modifier.fillMaxWidth().height(132.dp).clickable(onClick = onOpen))
            IconButton(
                onClick = { appState.toggleFavorite(dish) },
                modifier = Modifier.align(Alignment.TopEnd).padding(6.dp).size(34.dp),
            ) {
                SaborIcon(if (appState.isFavorite(dish)) "heart.fill" else "heart", tint = MaterialTheme.colorScheme.primary)
            }
        }
        Column(Modifier.padding(SaborSpacing.md), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            SaborRating(dish.rating, dish.reviews)
            Text(dish.name, style = MaterialTheme.typography.titleSmall, maxLines = 2)
            Text(dish.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("PRECIO", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(dish.priceText, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
                }
                SaborAddButton(onClick = { appState.addToCart(dish) })
            }
        }
    }
}

@Composable
fun MenuScreen(state: AppUiState, appState: AppState, onNavigate: (String) -> Unit) {
    var showFilters by remember { mutableStateOf(false) }
    var sortBy by remember { mutableStateOf("Popularidad") }
    var veganOnly by remember { mutableStateOf(false) }
    var glutenFreeOnly by remember { mutableStateOf(false) }
    var fastDeliveryOnly by remember { mutableStateOf(false) }
    var maxPrice by remember { mutableIntStateOf(30) }
    val filteredDishes = state.filteredDishes.filter { dish ->
        (!veganOnly || "Vegano" in dish.tags) &&
            (!glutenFreeOnly || "Sin Gluten" in dish.tags) &&
            (!fastDeliveryOnly || dish.isReadyWithin30Minutes()) &&
            dish.price <= maxPrice
    }
    val dishes = when (sortBy) {
        "Precio" -> filteredDishes.sortedBy(Dish::price)
        "Valoración" -> filteredDishes.sortedByDescending(Dish::rating)
        else -> filteredDishes
    }
    val activeFilterCount = listOf(veganOnly, glutenFreeOnly, fastDeliveryOnly).count { it } +
        if (maxPrice < 30) 1 else 0

    Box(Modifier.fillMaxSize()) {
        SaborScrollableScreen(bottomPadding = if (state.cartCount > 0) 110.dp else 24.dp) {
            SaborTopBar(
                location = state.addressLabel,
                onNotifications = { onNavigate("notifications") },
                onCart = { onNavigate("cart") },
            )
            SaborTextField(
                label = "",
                value = state.searchText,
                onValueChange = { appState.updateState { current -> current.copy(searchText = it) } },
                placeholder = "Buscar por plato o ingrediente...",
                icon = "search",
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { sortBy = if (sortBy == "Popularidad") "Precio" else if (sortBy == "Precio") "Valoración" else "Popularidad" }) {
                    Text("Ordenar: $sortBy  ⌄")
                }
                OutlinedButton(onClick = { showFilters = true }) { Text("Filtrar  $activeFilterCount") }
            }
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(SaborSpacing.sm)) {
                MockData.categories.forEach { category ->
                    SaborChip(
                        category,
                        selected = state.selectedCategory == category,
                        onClick = { appState.updateState { it.copy(selectedCategory = category) } },
                    )
                }
            }
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(SaborSpacing.sm)) {
                SaborChip("🌿  Vegano", selected = veganOnly) { veganOnly = !veganOnly }
                SaborChip("🌾  Sin Gluten", selected = glutenFreeOnly) { glutenFreeOnly = !glutenFreeOnly }
                SaborChip("⚡  Menos de 30 min", selected = fastDeliveryOnly) {
                    fastDeliveryOnly = !fastDeliveryOnly
                }
            }
            Column {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("Menú de Temporada", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.weight(1f))
                    SaborTag("Cocina activa", icon = "check.circle")
                }
                Text("${dishes.size} platos artesanos disponibles hoy", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            dishes.forEach { dish ->
                DishCardWide(dish, state, appState, onOpen = { onNavigate("dish/${dish.id}") })
            }
        }
        if (state.cartCount > 0) {
            Surface(
                Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(12.dp),
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 12.dp,
            ) {
                Row(
                    Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    SaborIcon("bag.fill", tint = MaterialTheme.colorScheme.primary)
                    Column(Modifier.weight(1f)) {
                        Text("Mi selección · ${state.cartCount} platos", style = MaterialTheme.typography.labelSmall)
                        Text(Dish.formatPrice(state.subtotal), color = MaterialTheme.colorScheme.primary)
                    }
                    Button(onClick = { onNavigate("cart") }) { Text("Ver cesta  →") }
                }
            }
        }
    }

    if (showFilters) {
        AlertDialog(
            onDismissRequest = { showFilters = false },
            title = { Text("Filtros") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Precio máximo: ${Dish.formatPrice(maxPrice.toDouble())}")
                    androidx.compose.material3.Slider(
                        value = maxPrice.toFloat(),
                        onValueChange = { maxPrice = it.toInt().coerceIn(5, 30) },
                        valueRange = 5f..30f,
                    )
                    SaborToggleRow(
                        title = "Vegano",
                        checked = veganOnly,
                        onCheckedChange = { veganOnly = it },
                        icon = "leaf.fill",
                    )
                    SaborToggleRow(
                        title = "Sin Gluten",
                        checked = glutenFreeOnly,
                        onCheckedChange = { glutenFreeOnly = it },
                        icon = "leaf.fill",
                    )
                    SaborToggleRow(
                        title = "Entrega rápida (< 30 min)",
                        checked = fastDeliveryOnly,
                        onCheckedChange = { fastDeliveryOnly = it },
                        icon = "clock",
                    )
                }
            },
            confirmButton = { TextButton(onClick = { showFilters = false }) { Text("Aplicar filtros") } },
            dismissButton = { TextButton(onClick = { showFilters = false }) { Text("Cerrar") } },
        )
    }
}

private fun Dish.isReadyWithin30Minutes(): Boolean {
    val maxMinutes = Regex("""\d+""")
        .findAll(prepTime)
        .mapNotNull { it.value.toIntOrNull() }
        .maxOrNull()
    return maxMinutes == null || maxMinutes <= 30
}

@Composable
private fun DishCardWide(
    dish: Dish,
    state: AppUiState,
    appState: AppState,
    onOpen: () -> Unit,
) {
    SaborCard(Modifier.fillMaxWidth()) {
        Box {
            SaborImage(dish.imageKey, Modifier.fillMaxWidth().height(168.dp).clickable(onClick = onOpen))
            Row(
                Modifier.fillMaxWidth().align(Alignment.TopCenter).padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                if (dish.badge != null) SaborTag(dish.badge.uppercase(), icon = "flame", tint = MaterialTheme.colorScheme.onPrimary)
                IconButton(onClick = { appState.toggleFavorite(dish) }, modifier = Modifier.size(36.dp)) {
                    SaborIcon(if (appState.isFavorite(dish)) "heart.fill" else "heart", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
        Column(Modifier.padding(SaborSpacing.md), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    dish.name,
                    modifier = Modifier.weight(1f).clickable(onClick = onOpen),
                    style = MaterialTheme.typography.titleMedium,
                )
                SaborRating(dish.rating, dish.reviews)
            }
            Text(dish.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                dish.tags.take(3).forEach { SaborTag(it, icon = if ("Gluten" in it || "Vegetariano" in it) "leaf.fill" else null) }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("PRECIO", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(dish.priceText, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
                        dish.oldPriceText?.let {
                            Text(it, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
                Spacer(Modifier.weight(1f))
                Button(onClick = { appState.addToCart(dish) }) { Text("+  Añadir") }
            }
        }
    }
}

@Composable
fun FavoritesScreen(state: AppUiState, appState: AppState, onNavigate: (String) -> Unit) {
    var query by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableIntStateOf(0) }
    var showEmpty by remember { mutableStateOf(false) }
    val filters = listOf("Todos", "Platos Principales", "Postres", "Ofertas")
    val favorites = state.favoriteDishes.filter { dish ->
        !showEmpty &&
            dish.name.contains(query, ignoreCase = true) &&
            when (selectedFilter) {
                1 -> dish.category != "Postres" && dish.oldPrice == null
                2 -> dish.category == "Postres"
                3 -> dish.oldPrice != null
                else -> true
            }
    }

    SaborScrollableScreen(bottomPadding = 110.dp) {
        SaborTopBar(
            location = state.addressLabel,
            onNotifications = { onNavigate("notifications") },
            onCart = { onNavigate("cart") },
        )
        Column(verticalArrangement = Arrangement.spacedBy(SaborSpacing.sm)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Mis Favoritos", style = MaterialTheme.typography.headlineLarge)
                Spacer(Modifier.width(8.dp))
                Text(state.favorites.size.toString(), color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.weight(1f))
                TextButton(onClick = {
                    appState.updateState { it.copy(favorites = emptySet()) }
                }) { Text("Limpiar") }
            }
            Text("Tus elecciones gastronómicas predilectas", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(SaborSpacing.sm)) {
                SaborChip("Guardados (${state.favorites.size})", selected = !showEmpty) { showEmpty = false }
                SaborChip("Ver estado vacío", selected = showEmpty) { showEmpty = true }
            }
        }
        SaborTextField("Buscar", query, { query = it }, placeholder = "Buscar en mis favoritos...", icon = "search")
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(SaborSpacing.sm)) {
            filters.forEachIndexed { index, filter ->
                SaborChip(
                    if (index == 0) "$filter (${state.favorites.size})" else filter,
                    selected = selectedFilter == index,
                    onClick = { selectedFilter = index },
                )
            }
        }
        if (favorites.isEmpty()) {
            SaborCard(Modifier.fillMaxWidth()) {
                Column(
                    Modifier.fillMaxWidth().padding(SaborSpacing.xl),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(SaborSpacing.md),
                ) {
                    SaborIcon("heart", Modifier.size(42.dp), MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                    Text("Aún no tienes platos favoritos", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Explora nuestra carta gastronómica y pulsa en el corazón de los platos que más te gusten.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Button(onClick = { onNavigate("menu") }) {
                        Text("Explorar la carta completa")
                    }
                }
            }
        } else {
            favorites.forEach { dish ->
                DishCardWide(dish, state, appState, onOpen = { onNavigate("dish/${dish.id}") })
            }
            Column(verticalArrangement = Arrangement.spacedBy(SaborSpacing.md)) {
                SaborSectionHeader("Recomendados por el Chef", icon = "star")
                Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(SaborSpacing.md)) {
                    state.dishes.filter { it.name in setOf("Ramen Tonkotsu", "Gyozas de Ibérico", "Bowl Burrata & Higos") }
                        .forEach { CompactDishCard(it, state, appState) { onNavigate("dish/${it.id}") } }
                }
            }
        }
    }
}

@Composable
fun OrdersScreen(state: AppUiState, onNavigate: (String) -> Unit) {
    SaborScrollableScreen(bottomPadding = 110.dp) {
        SaborTopBar(
            title = "Mis pedidos",
            onNotifications = { onNavigate("notifications") },
            onCart = { onNavigate("cart") },
        )
        Text("Tu historial gastronómico", color = MaterialTheme.colorScheme.onSurfaceVariant)
        state.orders.forEach { order ->
            SaborCard(Modifier.fillMaxWidth().clickable { onNavigate("tracking") }) {
                Column(Modifier.padding(SaborSpacing.md), verticalArrangement = Arrangement.spacedBy(SaborSpacing.md)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(order.reference, style = MaterialTheme.typography.titleMedium)
                            Text("${order.date} · ${order.title}", style = MaterialTheme.typography.bodySmall)
                        }
                        SaborTag(
                            order.status.label,
                            tint = if (order.status == OrderStatus.CANCELLED) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.secondary,
                        )
                    }
                    Text(order.itemsSummary, style = MaterialTheme.typography.bodySmall, maxLines = 2)
                    order.eta?.let {
                        Text("Entrega: $it", style = MaterialTheme.typography.bodySmall)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("${order.dishCount} platos · ${order.payment}", style = MaterialTheme.typography.labelSmall)
                        Spacer(Modifier.weight(1f))
                        Text(Dish.formatPrice(order.total), color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
                    }
                    if (order.status == OrderStatus.IN_PROGRESS) {
                        Button(onClick = { onNavigate("tracking") }, modifier = Modifier.fillMaxWidth()) {
                            Text("Seguir pedido")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(state: AppUiState, appState: AppState, onNavigate: (String) -> Unit) {
    SaborScrollableScreen(bottomPadding = 110.dp) {
        SaborTopBar(title = "Mi perfil", onCart = { onNavigate("cart") })
        SaborCard(Modifier.fillMaxWidth()) {
            Row(
                Modifier.padding(SaborSpacing.md),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(SaborSpacing.md),
            ) {
                SaborImage("profile", Modifier.size(68.dp).clip(CircleShape))
                Column(Modifier.weight(1f)) {
                    Text(state.user.name, style = MaterialTheme.typography.titleLarge)
                    Text(state.user.email, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    SaborTag("Sabor Club · ${state.user.points} puntos")
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(SaborSpacing.sm)) {
            StatCard("Pedidos", state.user.orders.toString(), Modifier.weight(1f))
            StatCard("Favoritos", state.favorites.size.toString(), Modifier.weight(1f))
            StatCard("Direcciones", state.addresses.size.toString(), Modifier.weight(1f))
        }
        SaborCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(horizontal = SaborSpacing.md)) {
                SaborNavRow("Mis direcciones", "location", subtitle = "Gestiona tus lugares de entrega") {
                    onNavigate("addresses")
                }
                SaborNavRow("Ajustes y preferencias", "settings", subtitle = state.appearance.label) {
                    onNavigate("settings")
                }
                SaborNavRow("Notificaciones", "notifications", badgeText = "${state.notifications.count { it.unread }} nuevas") {
                    onNavigate("notifications")
                }
                SaborNavRow("Mis pedidos", "orders") { onNavigate("orders") }
                SaborNavRow("Favoritos", "heart.fill") { onNavigate("favorites") }
            }
        }
        SaborPrimaryButton(
            title = "Cerrar sesión",
            icon = "arrow.right",
            onClick = appState::signOut,
        )
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    SaborCard(modifier) {
        Column(
            Modifier.fillMaxWidth().padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(value, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun NotificationsScreen(state: AppUiState, onBack: () -> Unit, onTrack: () -> Unit) {
    SaborScrollableScreen {
        SaborTopBar(title = "Notificaciones", onBack = onBack)
        state.notifications.groupBy { it.group }.forEach { (group, notifications) ->
            Column(verticalArrangement = Arrangement.spacedBy(SaborSpacing.sm)) {
                Text(group, style = MaterialTheme.typography.titleMedium)
                notifications.forEach { notification ->
                    SaborCard(Modifier.fillMaxWidth().clickable(onClick = onTrack)) {
                        Row(
                            Modifier.padding(SaborSpacing.md),
                            horizontalArrangement = Arrangement.spacedBy(SaborSpacing.md),
                        ) {
                            SaborIcon(notification.icon, tint = MaterialTheme.colorScheme.primary)
                            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(notification.title, modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleSmall)
                                    if (notification.unread) SaborTag("Nuevo")
                                }
                                Text(notification.body, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${notification.kind} · ${notification.time}", style = MaterialTheme.typography.labelSmall)
                                notification.primaryAction?.let { TextButton(onClick = onTrack) { Text(it) } }
                            }
                        }
                    }
                }
            }
        }
    }
}
