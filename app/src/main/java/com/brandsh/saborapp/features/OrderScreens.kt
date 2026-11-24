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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brandsh.saborapp.data.MockData
import com.brandsh.saborapp.designsystem.LocalSaborPalette
import com.brandsh.saborapp.designsystem.SaborCard
import com.brandsh.saborapp.designsystem.SaborCanvas
import com.brandsh.saborapp.designsystem.SaborIcon
import com.brandsh.saborapp.designsystem.SaborImage
import com.brandsh.saborapp.designsystem.SaborPrimaryButton
import com.brandsh.saborapp.designsystem.SaborRating
import com.brandsh.saborapp.designsystem.SaborSectionHeader
import com.brandsh.saborapp.designsystem.SaborSpacing
import com.brandsh.saborapp.designsystem.SaborStepper
import com.brandsh.saborapp.designsystem.SaborTag
import com.brandsh.saborapp.designsystem.SaborTextField
import com.brandsh.saborapp.designsystem.SaborTopBar
import com.brandsh.saborapp.models.Dish
import com.brandsh.saborapp.models.OptionGroup
import com.brandsh.saborapp.models.OrderStatus
import com.brandsh.saborapp.models.StepState
import com.brandsh.saborapp.stores.AppState
import com.brandsh.saborapp.stores.AppUiState

@Composable
private fun OrderScreenColumn(
    modifier: Modifier = Modifier,
    bottomPadding: androidx.compose.ui.unit.Dp = 24.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(modifier.fillMaxSize()) {
        SaborCanvas()
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = SaborSpacing.margin)
                .padding(top = SaborSpacing.sm, bottom = bottomPadding),
            verticalArrangement = Arrangement.spacedBy(SaborSpacing.lg),
            content = content,
        )
    }
}

@Composable
fun DishDetailScreen(dish: Dish, appState: AppState, onBack: () -> Unit) {
    val groups = remember(dish.id) { MockData.optionGroups(dish) }
    val selected = remember(dish.id) { mutableStateMapOf<String, String>() }
    var quantity by remember(dish.id) { mutableIntStateOf(1) }
    var showNutrition by remember(dish.id) { mutableStateOf(false) }

    LaunchedEffect(dish.id) {
        groups.filterNot { it.multiSelect }.forEach { group ->
            group.items.firstOrNull { it.isDefault }?.let { selected[group.id] = it.id }
        }
    }

    val selectedExtras = groups.flatMap { group ->
        group.items.filter { item ->
            if (group.multiSelect) item.id in selected.values else selected[group.id] == item.id
        }
    }.sumOf { it.extra }
    val unitPrice = dish.price + selectedExtras

    Box(Modifier.fillMaxSize()) {
        SaborCanvas()
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            Box(Modifier.fillMaxWidth().height(320.dp)) {
                SaborImage(dish.imageKey, Modifier.fillMaxSize())
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = SaborSpacing.margin, vertical = 44.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    CircleAction("arrow.left", onBack)
                    CircleAction(
                        if (appState.isFavorite(dish)) "heart.fill" else "heart",
                    ) { appState.toggleFavorite(dish) }
                }
            }
            Column(
                Modifier.padding(SaborSpacing.margin),
                verticalArrangement = Arrangement.spacedBy(SaborSpacing.md),
            ) {
                SaborTag(dish.badge ?: "Plato de Autor", icon = "flame")
                Row(verticalAlignment = Alignment.Top) {
                    Text(dish.name, modifier = Modifier.weight(1f), style = MaterialTheme.typography.headlineMedium)
                    Spacer(Modifier.width(10.dp))
                    Text(dish.priceText, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleLarge)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(SaborSpacing.sm), verticalAlignment = Alignment.CenterVertically) {
                    SaborRating(dish.rating, dish.reviews)
                    SaborTag(dish.prepTime, icon = "clock")
                    SaborTag("${dish.calories} kcal", icon = "flame")
                }
                Text(dish.description, color = MaterialTheme.colorScheme.onSurfaceVariant)
                SaborCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(SaborSpacing.md), verticalArrangement = Arrangement.spacedBy(SaborSpacing.sm)) {
                        Text("Ingredientes seleccionados", style = MaterialTheme.typography.titleMedium)
                        MockData.ingredients.forEach { (icon, ingredient) ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                SaborIcon(icon, Modifier.size(16.dp), MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.width(8.dp))
                                Text(ingredient, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
                SaborCard(Modifier.fillMaxWidth()) {
                    Column {
                        Row(
                            Modifier.fillMaxWidth().clickable { showNutrition = !showNutrition }.padding(SaborSpacing.md),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            SaborIcon("info.circle.fill", tint = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.width(8.dp))
                            Text("Información nutricional y alérgenos", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleSmall)
                            SaborIcon(if (showNutrition) "chevron.up" else "chevron.down")
                        }
                        if (showNutrition) {
                            Text(
                                "Alérgenos: Lácteos · Gluten\nProteínas 42g     Grasas 36g     Carbohidratos 48g",
                                Modifier.padding(start = SaborSpacing.md, end = SaborSpacing.md, bottom = SaborSpacing.md),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
                SaborSectionHeader("Personaliza tu plato", actionTitle = "A TU GUSTO")
                groups.forEach { group ->
                    DishOptionGroup(group, selected[group.id]) { item ->
                        if (group.multiSelect && item.id in selected.values) {
                            selected.remove(group.id)
                        } else {
                            selected[group.id] = item.id
                        }
                    }
                }
            }
        }
        Surface(
            Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 12.dp,
        ) {
            Row(
                Modifier.padding(SaborSpacing.md),
                horizontalArrangement = Arrangement.spacedBy(SaborSpacing.md),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SaborStepper(quantity, onValueChange = { quantity = it })
                SaborPrimaryButton(
                    title = "Agregar al pedido · ${Dish.formatPrice(unitPrice * quantity)}",
                    icon = "bag.fill",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        val options = groups.flatMap { group ->
                            group.items.filter { item -> selected[group.id] == item.id }.map { it.name }
                        }
                        appState.addToCart(dish, quantity, options, unitPrice)
                        onBack()
                    },
                )
            }
        }
    }
}

@Composable
private fun CircleAction(icon: String, onClick: () -> Unit) {
    Surface(
        Modifier.size(40.dp).clickable(onClick = onClick),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
    ) {
        Box(contentAlignment = Alignment.Center) {
            SaborIcon(icon, Modifier.size(18.dp))
        }
    }
}

@Composable
private fun DishOptionGroup(group: OptionGroup, selectedId: String?, onSelect: (com.brandsh.saborapp.models.OptionItem) -> Unit) {
    SaborCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(SaborSpacing.md), verticalArrangement = Arrangement.spacedBy(SaborSpacing.sm)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(group.title, modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleSmall)
                Text(
                    if (group.required) "Obligatorio" else "Opcional",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
            group.items.forEach { item ->
                val isSelected = selectedId == item.id
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.06f)
                            else LocalSaborPalette.current.containerLow,
                        )
                        .clickable { onSelect(item) }
                        .padding(horizontal = 10.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    androidx.compose.material3.RadioButton(selected = isSelected, onClick = { onSelect(item) })
                    Text(item.name, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                    item.note?.let { Text(it, style = MaterialTheme.typography.labelSmall) }
                }
            }
        }
    }
}

@Composable
fun CartScreen(
    state: AppUiState,
    appState: AppState,
    onBack: () -> Unit,
    onCheckout: () -> Unit,
    onMenu: () -> Unit,
) {
    LaunchedEffect(Unit) { appState.seedCartIfEmpty() }
    Box(Modifier.fillMaxSize()) {
        OrderScreenColumn(bottomPadding = if (state.cart.isNotEmpty()) 116.dp else 24.dp) {
            SaborTopBar(title = "Carrito", onBack = onBack)
            SaborCard(Modifier.fillMaxWidth()) {
                Row(
                    Modifier.padding(SaborSpacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(SaborSpacing.md),
                ) {
                    SaborIcon("bicycle", Modifier.size(30.dp), MaterialTheme.colorScheme.secondary)
                    Column(Modifier.weight(1f)) {
                        Text("Entrega estimada", style = MaterialTheme.typography.labelSmall)
                        Text("25-35 min", style = MaterialTheme.typography.titleSmall)
                        Text("Calle Mayor 24, 3ºB", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    TextButton(onClick = {}) { Text("Cambiar") }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(SaborSpacing.md)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Tu pedido (${state.cartCount} platos)", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = onMenu) { Text("Añadir más") }
                }
                if (state.cart.isEmpty()) {
                    Text("Tu carrito está vacío", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                state.cart.forEach { line ->
                    SaborCard(Modifier.fillMaxWidth()) {
                        Row(
                            Modifier.padding(SaborSpacing.sm),
                            horizontalArrangement = Arrangement.spacedBy(SaborSpacing.sm),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            SaborImage(line.dish.imageKey, Modifier.size(68.dp).clip(RoundedCornerShape(8.dp)))
                            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(line.dish.name, Modifier.weight(1f), style = MaterialTheme.typography.titleSmall, maxLines = 1)
                                    IconButton(onClick = { appState.remove(line) }) {
                                        SaborIcon("trash", Modifier.size(18.dp), MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                                if (line.options.isNotEmpty()) {
                                    Text(line.options.joinToString(" · "), style = MaterialTheme.typography.bodySmall, maxLines = 1)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(Dish.formatPrice(line.lineTotal), color = MaterialTheme.colorScheme.primary)
                                    Spacer(Modifier.weight(1f))
                                    SaborStepper(
                                        line.quantity,
                                        onValueChange = { count ->
                                            if (count > line.quantity) appState.inc(line) else appState.dec(line)
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(SaborSpacing.md)) {
                SaborSectionHeader("¿Deseas agregar bebida?", icon = "wineglass.fill", actionTitle = "Maridaje ideal")
                Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(SaborSpacing.sm)) {
                    state.dishes.filter { it.category == "Bebidas" }.forEach { drink ->
                        SaborCard(Modifier.width(250.dp)) {
                            Row(
                                Modifier.padding(SaborSpacing.sm),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(SaborSpacing.sm),
                            ) {
                                SaborImage(drink.imageKey, Modifier.size(44.dp).clip(RoundedCornerShape(8.dp)))
                                Column(Modifier.weight(1f)) {
                                    Text(drink.name, style = MaterialTheme.typography.labelMedium, maxLines = 1)
                                    Text(drink.priceText, color = MaterialTheme.colorScheme.primary)
                                }
                                CircleAction("plus") { appState.addToCart(drink) }
                            }
                        }
                    }
                }
            }
            SaborCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(SaborSpacing.md)) {
                    androidx.compose.material3.Switch(
                        checked = state.ecoCutlery,
                        onCheckedChange = { enabled -> appState.updateState { it.copy(ecoCutlery = enabled) } },
                    )
                    Text("Cubiertos biodegradables", style = MaterialTheme.typography.titleSmall)
                    Text("Ayuda a reducir residuos de un solo uso", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            SaborCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(SaborSpacing.md), verticalArrangement = Arrangement.spacedBy(SaborSpacing.sm)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SaborIcon("tag.fill", tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Text("Cupón SABOR25", modifier = Modifier.weight(1f))
                        TextButton(onClick = {
                            appState.updateState { it.copy(couponApplied = !it.couponApplied) }
                        }) { Text(if (state.couponApplied) "Aplicado" else "Aplicar") }
                    }
                    if (state.couponApplied) SaborTag("Cupón SABOR25 · descuento de ${Dish.formatPrice(state.discount)}")
                }
            }
            SaborCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(SaborSpacing.md), verticalArrangement = Arrangement.spacedBy(SaborSpacing.sm)) {
                    Text("Resumen del pedido", style = MaterialTheme.typography.titleLarge)
                    SummaryRow("Subtotal", state.subtotal)
                    SummaryRow("Coste de entrega", state.deliveryFee)
                    SummaryRow("Tarifa de servicio", state.serviceFee)
                    if (state.discount > 0) SummaryRow("Descuento promocional", -state.discount)
                    androidx.compose.material3.HorizontalDivider(color = LocalSaborPalette.current.sand)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text("Total a pagar", style = MaterialTheme.typography.titleMedium)
                            Text("Impuestos incluidos (10% IVA)", style = MaterialTheme.typography.bodySmall)
                        }
                        Text(Dish.formatPrice(state.total), style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
        if (state.cart.isNotEmpty()) {
            Surface(
                Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 12.dp,
            ) {
                Row(Modifier.padding(SaborSpacing.md), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Total con descuento", style = MaterialTheme.typography.labelSmall)
                        Text(Dish.formatPrice(state.total), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                    }
                    Button(onClick = onCheckout) { Text("Continuar al pago  →") }
                }
            }
        }
    }
}

@Composable
private fun SummaryRow(title: String, amount: Double) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(title, modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            if (amount < 0) "-${Dish.formatPrice(-amount)}" else Dish.formatPrice(amount),
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Composable
fun CheckoutScreen(
    state: AppUiState,
    appState: AppState,
    onBack: () -> Unit,
    onAddresses: () -> Unit,
    onOrderPlaced: (paymentMethod: String, scheduledTime: String?) -> Unit,
) {
    LaunchedEffect(Unit) { appState.seedCartIfEmpty() }
    var payment by remember { mutableIntStateOf(0) }
    var scheduled by remember { mutableStateOf(false) }
    var scheduledTime by remember { mutableStateOf("20:30") }
    var note by remember { mutableStateOf("") }
    val paymentMethods = listOf("Mastercard •••• 4829", "Apple Pay", "Pago en efectivo")
    val hasValidScheduledTime = Regex("""(?:[01]\d|2[0-3]):[0-5]\d""").matches(scheduledTime)

    Box(Modifier.fillMaxSize()) {
        OrderScreenColumn(bottomPadding = 116.dp) {
            SaborTopBar(title = "Finalizar pedido", onBack = onBack)
            SaborCard(Modifier.fillMaxWidth()) {
                Row(Modifier.padding(SaborSpacing.md), verticalAlignment = Alignment.CenterVertically) {
                    SaborIcon("bicycle", Modifier.size(30.dp), MaterialTheme.colorScheme.primary)
                    Column(Modifier.weight(1f).padding(start = 12.dp)) {
                        Text("Tiempo estimado", style = MaterialTheme.typography.labelSmall)
                        Text(
                            if (scheduled) "Programado para las $scheduledTime" else "25 - 35 minutos",
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                    SaborTag("En directo")
                }
            }
            SaborCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(SaborSpacing.md), verticalArrangement = Arrangement.spacedBy(SaborSpacing.sm)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Dirección de entrega", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleSmall)
                        TextButton(onClick = onAddresses) { Text("Cambiar") }
                    }
                    state.addresses.firstOrNull()?.let { address ->
                        Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            SaborIcon(address.icon, tint = MaterialTheme.colorScheme.primary)
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(address.label, style = MaterialTheme.typography.titleSmall)
                                    Spacer(Modifier.width(6.dp))
                                    if (address.isDefault) SaborTag("Principal")
                                }
                                Text(address.detail, style = MaterialTheme.typography.bodySmall)
                                Text(address.note, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
            SaborCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(SaborSpacing.md), verticalArrangement = Arrangement.spacedBy(SaborSpacing.sm)) {
                    Text("Momento de entrega", style = MaterialTheme.typography.titleSmall)
                    Row(horizontalArrangement = Arrangement.spacedBy(SaborSpacing.sm)) {
                        Button(
                            onClick = { scheduled = false },
                            modifier = Modifier.weight(1f),
                        ) { Text(if (scheduled) "Lo antes posible" else "✓  Lo antes posible") }
                        OutlinedButton(
                            onClick = { scheduled = true },
                            modifier = Modifier.weight(1f),
                        ) { Text(if (scheduled) "✓  Programar hora" else "Programar hora") }
                    }
                    if (scheduled) {
                        SaborTextField(
                            label = "Hora de entrega (24 h)",
                            value = scheduledTime,
                            onValueChange = { input ->
                                scheduledTime = input.filter { it.isDigit() || it == ':' }.take(5)
                            },
                            placeholder = "20:30",
                            keyboardType = KeyboardType.Text,
                        )
                        if (!hasValidScheduledTime) {
                            Text(
                                "Introduce una hora válida entre 00:00 y 23:59.",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }
            }
            SaborCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(SaborSpacing.md), verticalArrangement = Arrangement.spacedBy(SaborSpacing.sm)) {
                    Text("Método de pago", style = MaterialTheme.typography.titleSmall)
                    listOf(
                        "•••• 4829" to "Mastercard · Expira 09/27",
                        "Apple Pay" to state.user.email,
                        "Pago en efectivo" to "Importe exacto recomendado",
                    ).forEachIndexed { index, (title, subtitle) ->
                        Row(
                            Modifier.fillMaxWidth().clickable { payment = index }.padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            SaborIcon(if (index == 2) "creditcard.fill" else "creditcard", tint = MaterialTheme.colorScheme.primary)
                            Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                                Text(title, style = MaterialTheme.typography.titleSmall)
                                Text(subtitle, style = MaterialTheme.typography.bodySmall)
                            }
                            RadioButton(selected = payment == index, onClick = { payment = index })
                        }
                    }
                }
            }
            SaborCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(SaborSpacing.md)) {
                    Text("Notas para cocina o repartidor", style = MaterialTheme.typography.titleSmall)
                    SaborTextField(
                        label = "",
                        value = note,
                        onValueChange = { note = it },
                        placeholder = "Ej: sin cebolla, dejar en conserjería...",
                    )
                }
            }
            SaborCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(SaborSpacing.md), verticalArrangement = Arrangement.spacedBy(SaborSpacing.sm)) {
                    SaborSectionHeader("Resumen del pedido", actionTitle = "${state.cartCount} platos")
                    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        state.cart.take(3).forEach {
                            SaborImage(it.dish.imageKey, Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)))
                        }
                    }
                    Text(state.cart.joinToString(", ") { it.dish.name.substringBefore(" ") }, style = MaterialTheme.typography.bodySmall)
                    SummaryRow("Subtotal", state.subtotal)
                    SummaryRow("Gastos de envío", state.deliveryFee)
                    SummaryRow("Tarifa de servicio", state.serviceFee)
                    SummaryRow("Cupón SABOR25", -state.discount)
                    androidx.compose.material3.HorizontalDivider(color = LocalSaborPalette.current.sand)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Total final", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                        Text(Dish.formatPrice(state.total), style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            SaborTag("Este pedido compensa su huella de carbono y utiliza envases compostables.", icon = "leaf.fill")
        }
        Surface(
            Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 12.dp,
        ) {
            Column(Modifier.padding(SaborSpacing.md), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Button(
                    onClick = {
                        onOrderPlaced(
                            paymentMethods[payment],
                            scheduledTime.takeIf { scheduled },
                        )
                    },
                    enabled = !scheduled || hasValidScheduledTime,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                ) {
                    Text("🔒  Realizar pedido · ${Dish.formatPrice(state.total)}  →")
                }
                Text(
                    "Pago cifrado SSL de 256 bits y garantía Sabor",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

@Composable
fun OrderConfirmedScreen(state: AppUiState, onTracking: () -> Unit, onHome: () -> Unit) {
    OrderScreenColumn {
        Column(
            Modifier.fillMaxWidth().padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SaborSpacing.md),
        ) {
            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.14f)) {
                Box(Modifier.size(96.dp), contentAlignment = Alignment.Center) {
                    SaborIcon("checkmark", Modifier.size(48.dp), MaterialTheme.colorScheme.secondary)
                }
            }
            Text("¡Pedido confirmado con éxito!", style = MaterialTheme.typography.headlineMedium)
            Text(
                "La cocina de Sabor ha recibido tu orden y ya está encendiendo los fogones.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        SaborCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(SaborSpacing.md), verticalArrangement = Arrangement.spacedBy(SaborSpacing.md)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Referencia", style = MaterialTheme.typography.labelSmall)
                        Text(state.lastOrderReference, style = MaterialTheme.typography.titleSmall)
                    }
                    SaborTag("En cocina", icon = "flame")
                }
                androidx.compose.material3.HorizontalDivider(color = LocalSaborPalette.current.sand)
                InfoRow(
                    "clock",
                    "Tiempo estimado",
                    state.orders.firstOrNull()?.eta ?: "25 - 35 min",
                )
                InfoRow("location", "Dirección de entrega", state.addresses.firstOrNull()?.detail ?: "Calle Mayor 24")
                InfoRow("creditcard", "Método de pago", state.orders.firstOrNull()?.payment ?: "Mastercard")
            }
        }
        SaborCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(SaborSpacing.md), verticalArrangement = Arrangement.spacedBy(SaborSpacing.sm)) {
                Text("Platos en preparación (${state.orders.firstOrNull()?.dishCount ?: 0})", style = MaterialTheme.typography.titleSmall)
                state.orders.firstOrNull()?.imageKeys?.forEach { key ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SaborImage(key, Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)))
                        Spacer(Modifier.width(10.dp))
                        Text(key, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
        SaborTag("Garantía de Frescura Sabor · Ingredientes frescos preparados al momento", icon = "leaf.fill")
        SaborPrimaryButton("Seguimiento en vivo", icon = "location", onClick = onTracking)
        OutlinedButton(onClick = onHome, modifier = Modifier.fillMaxWidth()) { Text("Ir al inicio") }
    }
}

@Composable
private fun InfoRow(icon: String, title: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        SaborIcon(icon, tint = MaterialTheme.colorScheme.primary)
        Column {
            Text(title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.titleSmall)
        }
    }
}

@Composable
fun TrackingScreen(state: AppUiState, onBack: () -> Unit) {
    val currentOrder = state.orders.firstOrNull { it.reference == state.lastOrderReference }
        ?: state.orders.firstOrNull()
    val scheduledDelivery = currentOrder?.eta?.startsWith("Programado") == true

    OrderScreenColumn {
        SaborTopBar(title = "Seguimiento", onBack = onBack)
        Box(
            Modifier.fillMaxWidth().height(190.dp).clip(RoundedCornerShape(24.dp))
                .background(Brush.linearGradient(listOf(MaterialTheme.colorScheme.secondary.copy(alpha = 0.22f), LocalSaborPalette.current.containerLow))),
        ) {
            Column(Modifier.fillMaxSize().padding(14.dp), verticalArrangement = Arrangement.SpaceBetween) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SaborTag("Sabor Bistro", icon = "flame")
                    SaborTag(currentOrder?.eta ?: "Carlos · 12 min", icon = "bicycle")
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    SaborTag("Mi Casa", icon = "house.fill")
                }
                Text("TRANSMISIÓN EN DIRECTO", color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.labelSmall)
            }
        }
        SaborCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(SaborSpacing.md), verticalArrangement = Arrangement.spacedBy(SaborSpacing.sm)) {
                SaborTag(
                    currentOrder?.eta ?: "En camino · 12 min aprox.",
                    icon = "location",
                )
                Text(
                    if (scheduledDelivery) "Tu pedido está programado"
                    else "El repartidor va hacia tu puerta",
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    if (scheduledDelivery) "Te entregaremos el pedido a la hora seleccionada."
                    else "Carlos ha recogido tu comida caliente y está a 2.4 km de tu dirección.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        SaborCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(SaborSpacing.md), verticalArrangement = Arrangement.spacedBy(SaborSpacing.md)) {
                MockData.trackingSteps.forEachIndexed { index, step ->
                    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Surface(
                            shape = CircleShape,
                            color = if (step.state == StepState.PENDING) LocalSaborPalette.current.container
                            else MaterialTheme.colorScheme.secondary,
                        ) {
                            Box(Modifier.size(34.dp), contentAlignment = Alignment.Center) {
                                SaborIcon(
                                    step.icon,
                                    Modifier.size(16.dp),
                                    if (step.state == StepState.PENDING) MaterialTheme.colorScheme.onSurfaceVariant
                                    else MaterialTheme.colorScheme.onSecondary,
                                )
                            }
                        }
                        Column(Modifier.weight(1f)) {
                            Text(step.title, style = MaterialTheme.typography.titleSmall)
                            Text(step.subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(step.time, style = MaterialTheme.typography.labelSmall)
                    }
                    if (index < MockData.trackingSteps.lastIndex) {
                        androidx.compose.material3.HorizontalDivider(
                            Modifier.padding(start = 16.dp),
                            color = if (step.state == StepState.DONE) MaterialTheme.colorScheme.secondary else LocalSaborPalette.current.sand,
                        )
                    }
                }
            }
        }
        SaborCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(SaborSpacing.md), verticalArrangement = Arrangement.spacedBy(SaborSpacing.sm)) {
                Text("Tu repartidor asignado", style = MaterialTheme.typography.titleSmall)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(SaborSpacing.md)) {
                    SaborImage("Carlos M.", Modifier.size(54.dp).clip(CircleShape))
                    Column(Modifier.weight(1f)) {
                        Text("Carlos M. · VIP Sabor", style = MaterialTheme.typography.titleSmall)
                        SaborRating(4.9, 1240)
                        Text("Moto Honda ecológica · M-8291-ZX", style = MaterialTheme.typography.bodySmall)
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(SaborSpacing.sm)) {
                    OutlinedButton(onClick = {}, modifier = Modifier.weight(1f)) { Text("Llamar") }
                    OutlinedButton(onClick = {}, modifier = Modifier.weight(1f)) { Text("Mensaje") }
                }
            }
        }
        SaborCard(Modifier.fillMaxWidth()) {
            Row(Modifier.padding(SaborSpacing.md), verticalAlignment = Alignment.CenterVertically) {
                SaborIcon("lock.fill", tint = MaterialTheme.colorScheme.primary)
                Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                    Text("Código PIN de entrega", style = MaterialTheme.typography.labelSmall)
                    Text("4821", style = MaterialTheme.typography.titleLarge, letterSpacing = 4.sp)
                }
                SaborIcon("check.circle", Modifier.size(24.dp), MaterialTheme.colorScheme.primary)
            }
        }
        SaborCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(SaborSpacing.md), verticalArrangement = Arrangement.spacedBy(SaborSpacing.sm)) {
                Text("Pedido ${state.lastOrderReference}", style = MaterialTheme.typography.titleSmall)
                Text(
                    "Pagado con ${currentOrder?.payment ?: "Apple Pay"} · Total: " +
                        Dish.formatPrice(currentOrder?.total ?: 41.60),
                )
                currentOrder?.imageKeys?.forEach { key ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SaborImage(key, Modifier.size(40.dp).clip(RoundedCornerShape(6.dp)))
                        Text(key, Modifier.padding(start = 8.dp), style = MaterialTheme.typography.bodySmall)
                    }
                }
                Text("Total abonado", style = MaterialTheme.typography.titleSmall)
                Text(Dish.formatPrice(currentOrder?.total ?: 41.60), color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
