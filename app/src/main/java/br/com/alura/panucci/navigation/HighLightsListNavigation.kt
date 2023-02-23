package br.com.alura.panucci.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.alura.panucci.sampledata.sampleProducts
import br.com.alura.panucci.ui.screens.HighlightsListScreen

fun NavGraphBuilder.highlightsListScreen(navController: NavHostController) {
    composable(AppDestination.HighLight.route) {
        HighlightsListScreen(
            products = sampleProducts,
            onNavigateToCheckout = {
                navController.navigate(AppDestination.Checkout.route)
            },
            onNavigateToDetails = { productReceived ->
                val promoCode = "ALURA"
                navController.navigate("${AppDestination.ProductDetails.route}/${productReceived.id}?promoCode=${promoCode}")
            }
        )
    }
}