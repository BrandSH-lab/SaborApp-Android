package com.brandsh.saborapp.features

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.brandsh.saborapp.designsystem.SaborCard
import com.brandsh.saborapp.designsystem.SaborIcon
import com.brandsh.saborapp.designsystem.SaborPrimaryButton
import com.brandsh.saborapp.designsystem.SaborSpacing
import com.brandsh.saborapp.designsystem.SaborTag
import com.brandsh.saborapp.designsystem.SaborToggleRow
import com.brandsh.saborapp.designsystem.SaborTopBar
import com.brandsh.saborapp.models.AppearanceMode
import com.brandsh.saborapp.stores.AppState
import com.brandsh.saborapp.stores.AppUiState

@Composable
fun AddressesScreen(state: AppUiState, appState: AppState, onBack: () -> Unit) {
    var showNewAddress by remember { mutableStateOf(false) }

    SaborScrollableScreen {
        SaborTopBar(title = "Mis direcciones", onBack = onBack)
        Text("Elige dónde quieres recibir tus pedidos", color = MaterialTheme.colorScheme.onSurfaceVariant)
        state.addresses.forEach { address ->
            SaborCard(
                Modifier.fillMaxWidth().clickable {
                    appState.updateState { it.copy(addressLabel = address.label) }
                },
            ) {
                Column(
                    Modifier.padding(SaborSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(SaborSpacing.sm),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SaborIcon(address.icon, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(SaborSpacing.sm))
                        Text(address.label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                        if (address.label == state.addressLabel || (state.addressLabel.contains("Mayor") && address.isDefault)) {
                            SaborTag("Seleccionada")
                        } else {
                            TextButton(onClick = {
                                appState.updateState { it.copy(addressLabel = address.label) }
                            }) { Text("Usar") }
                        }
                    }
                    Text(address.detail, style = MaterialTheme.typography.bodyMedium)
                    Text(address.note, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    address.schedule?.let { Text(it, style = MaterialTheme.typography.labelSmall) }
                }
            }
        }
        SaborPrimaryButton("Añadir nueva dirección", icon = "plus", onClick = { showNewAddress = true })
    }

    if (showNewAddress) {
        AlertDialog(
            onDismissRequest = { showNewAddress = false },
            title = { Text("Nueva dirección") },
            text = { Text("La gestión de direcciones estará disponible próximamente.") },
            confirmButton = { TextButton(onClick = { showNewAddress = false }) { Text("Entendido") } },
        )
    }
}

@Composable
fun SettingsScreen(state: AppUiState, appState: AppState, onBack: () -> Unit) {
    var chooseAppearance by remember { mutableStateOf(false) }

    SaborScrollableScreen {
        SaborTopBar(title = "Ajustes y preferencias", onBack = onBack)
        SaborCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(SaborSpacing.md), verticalArrangement = Arrangement.spacedBy(SaborSpacing.md)) {
                Text("Apariencia", style = MaterialTheme.typography.titleMedium)
                Row(
                    Modifier.fillMaxWidth().clickable { chooseAppearance = true },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SaborIcon(state.appearance.icon, tint = MaterialTheme.colorScheme.primary)
                    Text(state.appearance.label, Modifier.weight(1f).padding(horizontal = 12.dp))
                    Text("Cambiar", color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        SaborCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(SaborSpacing.md), verticalArrangement = Arrangement.spacedBy(SaborSpacing.md)) {
                Text("Notificaciones y experiencia", style = MaterialTheme.typography.titleMedium)
                SaborToggleRow(
                    "Actualizaciones del pedido",
                    state.pushOrderUpdates,
                    { checked -> appState.updateState { it.copy(pushOrderUpdates = checked) } },
                    subtitle = "Avisos del progreso de tu entrega",
                    icon = "notifications",
                )
                SaborToggleRow(
                    "Ofertas y promociones",
                    state.promoOffers,
                    { checked -> appState.updateState { it.copy(promoOffers = checked) } },
                    icon = "tag.fill",
                )
                SaborToggleRow(
                    "Novedades del chef",
                    state.chefNewsletter,
                    { checked -> appState.updateState { it.copy(chefNewsletter = checked) } },
                    icon = "restaurant",
                )
                SaborToggleRow(
                    "Respuesta háptica",
                    state.hapticsEnabled,
                    { checked -> appState.updateState { it.copy(hapticsEnabled = checked) } },
                    icon = "phone",
                )
                SaborToggleRow(
                    "Acceso biométrico",
                    state.biometrics,
                    { checked -> appState.updateState { it.copy(biometrics = checked) } },
                    icon = "security",
                )
            }
        }
    }

    if (chooseAppearance) {
        AlertDialog(
            onDismissRequest = { chooseAppearance = false },
            title = { Text("Apariencia") },
            text = {
                Column {
                    AppearanceMode.entries.forEach { mode ->
                        Row(
                            Modifier.fillMaxWidth().clickable {
                                appState.updateState { it.copy(appearance = mode) }
                                chooseAppearance = false
                            },
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(selected = state.appearance == mode, onClick = {
                                appState.updateState { it.copy(appearance = mode) }
                                chooseAppearance = false
                            })
                            Text(mode.label)
                        }
                    }
                }
            },
            confirmButton = {},
        )
    }
}
