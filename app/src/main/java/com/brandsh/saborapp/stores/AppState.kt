package com.brandsh.saborapp.stores

import com.brandsh.saborapp.data.MockData
import com.brandsh.saborapp.models.Address
import com.brandsh.saborapp.models.AppNotification
import com.brandsh.saborapp.models.AppTab
import com.brandsh.saborapp.models.AppearanceMode
import com.brandsh.saborapp.models.CartLine
import com.brandsh.saborapp.models.Dish
import com.brandsh.saborapp.models.Order
import com.brandsh.saborapp.models.OrderStatus
import com.brandsh.saborapp.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

data class AppUiState(
    val tab: AppTab = AppTab.HOME,
    val isAuthenticated: Boolean = false,
    val showOnboarding: Boolean = true,
    val user: User = User(
        name = "Valentina González",
        email = "valentina@correo.com",
        phone = "+34 612 345 678",
        points = 840,
        orders = 14,
        favorites = 4,
        addresses = 3,
    ),
    val addressLabel: String = "Calle Mayor 24 • Centro",
    val dishes: List<Dish> = MockData.dishes,
    val searchText: String = "",
    val selectedCategory: String = "Todas",
    val favorites: Set<String> = setOf(
        "Hamburguesa Trufada Sabor",
        "Tagliatelle al Pesto de Pistacho",
        "Costillar Ibérico a Baja Temperatura",
        "Cheesecake de Queso Manchego",
    ),
    val cart: List<CartLine> = emptyList(),
    val couponApplied: Boolean = true,
    val ecoCutlery: Boolean = true,
    val orders: List<Order> = MockData.orders,
    val addresses: List<Address> = MockData.addresses,
    val notifications: List<AppNotification> = MockData.notifications,
    val appearance: AppearanceMode = AppearanceMode.SYSTEM,
    val hapticsEnabled: Boolean = true,
    val pushOrderUpdates: Boolean = true,
    val promoOffers: Boolean = true,
    val chefNewsletter: Boolean = false,
    val biometrics: Boolean = true,
    val toast: String? = null,
    val lastOrderReference: String = "#SB-84920",
) {
    val filteredDishes: List<Dish>
        get() {
            val query = searchText.trim()
            return dishes.filter { dish ->
                (selectedCategory == "Todas" || dish.category == selectedCategory) &&
                    (query.isEmpty() ||
                        dish.name.contains(query, ignoreCase = true) ||
                        dish.description.contains(query, ignoreCase = true))
            }
        }

    val favoriteDishes: List<Dish>
        get() = dishes.filter { it.name in favorites }

    val cartCount: Int
        get() = cart.sumOf(CartLine::quantity)

    val subtotal: Double
        get() = cart.sumOf(CartLine::lineTotal)

    val deliveryFee: Double
        get() = if (cart.isEmpty()) 0.0 else 2.50

    val serviceFee: Double
        get() = if (cart.isEmpty()) 0.0 else 0.90

    val discount: Double
        get() = if (couponApplied && cart.isNotEmpty()) 8.00 else 0.0

    val total: Double
        get() = (subtotal + deliveryFee + serviceFee - discount).coerceAtLeast(0.0)
}

class AppState(initialState: AppUiState = AppUiState()) {
    private val mutableState = MutableStateFlow(initialState)
    val state: StateFlow<AppUiState> = mutableState.asStateFlow()

    fun updateState(transform: (AppUiState) -> AppUiState) {
        mutableState.update(transform)
    }

    fun isFavorite(dish: Dish): Boolean = dish.name in state.value.favorites

    fun toggleFavorite(dish: Dish) {
        mutableState.update { current ->
            val wasFavorite = dish.name in current.favorites
            val favorites = if (wasFavorite) {
                current.favorites - dish.name
            } else {
                current.favorites + dish.name
            }
            current.copy(
                favorites = favorites,
                toast = if (wasFavorite) "Eliminado de favoritos" else "Añadido a favoritos",
            )
        }
    }

    fun addToCart(
        dish: Dish,
        quantity: Int = 1,
        options: List<String> = emptyList(),
        unitPrice: Double? = null,
    ) {
        mutableState.update { current ->
            val lineIndex = current.cart.indexOfFirst {
                it.dish.name == dish.name && it.options == options
            }
            val cart = if (lineIndex >= 0) {
                current.cart.mapIndexed { index, line ->
                    if (index == lineIndex) line.copy(quantity = line.quantity + quantity) else line
                }
            } else {
                current.cart + CartLine(
                    dish = dish,
                    quantity = quantity,
                    options = options,
                    unitPrice = unitPrice ?: dish.price,
                )
            }
            current.copy(
                cart = cart,
                toast = "${dish.name} añadido al pedido",
                user = current.user.copy(favorites = current.favorites.size),
            )
        }
    }

    fun inc(line: CartLine) {
        mutableState.update { current ->
            current.copy(
                cart = current.cart.map {
                    if (it.id == line.id) it.copy(quantity = it.quantity + 1) else it
                },
            )
        }
    }

    fun dec(line: CartLine) {
        mutableState.update { current ->
            current.copy(
                cart = current.cart.mapNotNull {
                    if (it.id != line.id) {
                        it
                    } else {
                        it.copy(quantity = it.quantity - 1).takeIf { updated -> updated.quantity > 0 }
                    }
                },
            )
        }
    }

    fun remove(line: CartLine) {
        mutableState.update { current ->
            current.copy(cart = current.cart.filterNot { it.id == line.id })
        }
    }

    fun clearCart() {
        mutableState.update { it.copy(cart = emptyList()) }
    }

    fun seedCartIfEmpty() {
        mutableState.update { current ->
            if (current.cart.isNotEmpty()) {
                current
            } else {
                val burger = current.dishes.first { it.name == "Hamburguesa Trufada Sabor" }
                val pasta = current.dishes.first { it.name == "Tagliatelle al Pesto de Pistacho" }
                val dessert = current.dishes.first { it.name == "Cheesecake de Queso Manchego" }
                current.copy(
                    cart = listOf(
                        CartLine(
                            dish = burger,
                            quantity = 1,
                            options = listOf("Al punto", "Bacon ahumado", "Patatas gajo"),
                            unitPrice = 16.00,
                        ),
                        CartLine(
                            dish = pasta,
                            quantity = 1,
                            options = listOf("Receta original"),
                            unitPrice = 15.20,
                        ),
                        CartLine(
                            dish = dessert,
                            quantity = 2,
                            unitPrice = 7.50,
                        ),
                    ),
                )
            }
        }
    }

    fun placeOrder(
        paymentMethod: String = "Mastercard •••• 4829",
        scheduledTime: String? = null,
    ) {
        val reference = "#SB-${Random.nextInt(85000, 90000)}"
        mutableState.update { current ->
            val order = Order(
                reference = reference,
                date = "Hoy",
                title = "Entrega prioritaria a domicilio",
                status = OrderStatus.IN_PROGRESS,
                total = current.total,
                payment = paymentMethod,
                itemsSummary = current.cart.joinToString(", ") { it.dish.name },
                imageKeys = current.cart.map { it.dish.imageKey },
                eta = scheduledTime?.let { "Programado para las $it" } ?: "En camino (~14:50)",
                dishCount = current.cart.size,
                progress = scheduledTime?.let { "Entrega programada para las $it" } ?: "Preparando tu pedido",
            )
            current.copy(
                lastOrderReference = reference,
                orders = listOf(order) + current.orders,
                cart = emptyList(),
            )
        }
    }

    fun signIn() {
        mutableState.update { it.copy(isAuthenticated = true, showOnboarding = false) }
    }

    fun signOut() {
        mutableState.update {
            it.copy(isAuthenticated = false, showOnboarding = true, cart = emptyList())
        }
    }
}
