package br.com.alura.panucci.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.alura.panucci.sampledata.sampleProducts
import br.com.alura.panucci.ui.screens.HighlightsListScreen

internal const val highlightsListRoute = "highlight"
fun NavGraphBuilder.highlightsListScreen(navController: NavHostController) {
    composable(highlightsListRoute) {
        HighlightsListScreen(
            products = sampleProducts,
            onNavigateToCheckout = {
//                navController.navigate(AppDestination.Checkout.route)
                navController.navigateToCheckout()
            },
            onNavigateToDetails = { productReceived ->
                val promoCode = "ALURA"
//                navController.navigate("${AppDestination.ProductDetails.route}/${productReceived.id}?promoCode=${promoCode}")
                navController.navigateToProductDetails(productReceived.id,promoCode)
            }
        )
    }
}

fun NavController.navigateToHighLightsList(navOptions: NavOptions? = null){
    navigate(highlightsListRoute, navOptions)
}