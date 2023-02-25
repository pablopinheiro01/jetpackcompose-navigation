package br.com.alura.panucci.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.composable
import br.com.alura.panucci.ui.screens.ProductDetailsScreen
import br.com.alura.panucci.ui.viewmodels.ProductDetailsViewModel

private const val productDetailsRoute = "ProductDetails"
private const val productIdArgument = "productId"
private const val cumpomArgument = "promoCode"
fun NavGraphBuilder.productDetailsScreen(navController: NavHostController) {
    composable(
        "${productDetailsRoute}/{$productIdArgument}?$cumpomArgument={promoCode}",
        arguments = listOf(
            navArgument("promoCode") { nullable = true }
        ),
    ) { backStackEntry ->

        val promoCode = backStackEntry.arguments?.getString(cumpomArgument)

        backStackEntry.arguments?.getString(productIdArgument)?.let { id ->
            val viewModel: ProductDetailsViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.findProductById(id)
                viewModel.applyDiscountPromotionalCode(promoCode)
            }

            ProductDetailsScreen(
                uiState = uiState,
                onNavigateToCheckout = {
                    navController.navigateToCheckout()
                },
                onTryFindProductAgain = {
                    viewModel.findProductById(id)
                },
                onBackStack = {
                    navController.navigateUp()
                },
            )
        }
            ?: LaunchedEffect(Unit) { navController.navigateUp() } //volta para a tela anterior porem possui integração com  deeplink

    }
}

fun NavController.navigateToProductDetails(id: String, cupom: String = "promoCode") {
    navigate("${productDetailsRoute}/$id?$cumpomArgument={$cupom}")
}