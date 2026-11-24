package com.brandsh.saborapp.stores

import com.brandsh.saborapp.models.AppTab
import com.brandsh.saborapp.models.Dish
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AppStateTest {
    @Test
    fun filtersDishesByCategoryAndCaseInsensitiveSearch() {
        val appState = AppState()
        appState.updateState {
            it.copy(tab = AppTab.MENU, selectedCategory = "Pastas", searchText = "PISTACHO")
        }

        assertEquals(
            listOf("Tagliatelle al Pesto de Pistacho"),
            appState.state.value.filteredDishes.map(Dish::name),
        )
    }

    @Test
    fun cartMergesMatchingOptionsAndPlacesOrderWithCalculatedTotal() {
        val appState = AppState()
        val dish = appState.state.value.dishes.first()

        appState.addToCart(dish, quantity = 1, options = listOf("Al punto"), unitPrice = 16.0)
        appState.addToCart(dish, quantity = 1, options = listOf("Al punto"), unitPrice = 20.0)

        assertEquals(2, appState.state.value.cartCount)
        assertEquals(27.4, appState.state.value.total, 0.001)

        appState.placeOrder()

        val state = appState.state.value
        assertTrue(state.cart.isEmpty())
        assertEquals(state.lastOrderReference, state.orders.first().reference)
        assertEquals(27.4, state.orders.first().total, 0.001)
        assertEquals(1, state.orders.first().dishCount)
    }

    @Test
    fun togglingFavoriteUpdatesFavoritesAndToast() {
        val appState = AppState()
        val dish = appState.state.value.dishes.first()

        appState.toggleFavorite(dish)

        assertFalse(appState.isFavorite(dish))
        assertEquals("Eliminado de favoritos", appState.state.value.toast)
    }

    @Test
    fun placeOrderStoresSelectedPaymentAndScheduledTime() {
        val appState = AppState()
        appState.addToCart(appState.state.value.dishes.first())

        appState.placeOrder(
            paymentMethod = "Apple Pay",
            scheduledTime = "20:30",
        )

        val order = appState.state.value.orders.first()
        assertEquals("Apple Pay", order.payment)
        assertEquals("Programado para las 20:30", order.eta)
        assertEquals("Entrega programada para las 20:30", order.progress)
    }
}
