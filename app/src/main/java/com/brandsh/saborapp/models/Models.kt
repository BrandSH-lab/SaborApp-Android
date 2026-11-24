package com.brandsh.saborapp.models

import java.text.NumberFormat
import java.util.Locale
import java.util.UUID

data class Dish(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val price: Double,
    val oldPrice: Double? = null,
    val rating: Double,
    val reviews: Int,
    val prepTime: String,
    val calories: Int,
    val category: String,
    val tags: List<String> = emptyList(),
    val badge: String? = null,
    val imageKey: String,
) {
    val imageUrl: String?
        get() = SaborImages.url(imageKey)

    val priceText: String
        get() = formatPrice(price)

    val oldPriceText: String?
        get() = oldPrice?.let(::formatPrice)

    companion object {
        fun formatPrice(value: Double): String {
            val formatter = NumberFormat.getNumberInstance(Locale("es", "ES")).apply {
                minimumFractionDigits = 2
                maximumFractionDigits = 2
            }
            return "${formatter.format(value)} €"
        }
    }
}

data class CartLine(
    val id: String = UUID.randomUUID().toString(),
    val dish: Dish,
    val quantity: Int,
    val options: List<String> = emptyList(),
    val unitPrice: Double,
) {
    val lineTotal: Double
        get() = unitPrice * quantity
}

data class Order(
    val id: String = UUID.randomUUID().toString(),
    val reference: String,
    val date: String,
    val title: String,
    val status: OrderStatus,
    val total: Double,
    val payment: String,
    val itemsSummary: String,
    val imageKeys: List<String>,
    val rating: Double? = null,
    val eta: String? = null,
    val dishCount: Int,
    val progress: String? = null,
)

enum class OrderStatus(val label: String) {
    IN_PROGRESS("En curso"),
    DELIVERED("Entregado"),
    CANCELLED("Cancelado"),
}

data class Address(
    val id: String = UUID.randomUUID().toString(),
    val label: String,
    val icon: String,
    val detail: String,
    val note: String,
    val phone: String? = null,
    val isDefault: Boolean,
    val schedule: String? = null,
)

data class AppNotification(
    val id: String = UUID.randomUUID().toString(),
    val icon: String,
    val kind: String,
    val time: String,
    val title: String,
    val body: String,
    val unread: Boolean,
    val group: String,
    val imageKey: String? = null,
    val price: Double? = null,
    val primaryAction: String? = null,
)

data class TrackingStep(
    val id: String = UUID.randomUUID().toString(),
    val icon: String,
    val title: String,
    val subtitle: String,
    val time: String,
    val state: StepState,
)

enum class StepState {
    DONE,
    ACTIVE,
    PENDING,
}

data class OptionItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val extra: Double,
    val note: String? = null,
    val isDefault: Boolean = false,
)

data class OptionGroup(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val required: Boolean,
    val items: List<OptionItem>,
    val multiSelect: Boolean = false,
)

data class User(
    val name: String,
    val email: String,
    val phone: String,
    val points: Int,
    val orders: Int,
    val favorites: Int,
    val addresses: Int,
)

enum class AppearanceMode(
    val label: String,
    val icon: String,
    val isDark: Boolean?,
) {
    LIGHT("Claro (Predeterminado)", "sun.max.fill", false),
    DARK("Oscuro", "moon.fill", true),
    SYSTEM("Automático del sistema", "circle.lefthalf.filled", null),
}

enum class AppTab {
    HOME,
    MENU,
    FAVORITES,
    ORDERS,
    PROFILE,
}
