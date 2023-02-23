package br.com.alura.panucci.navigation

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.alura.panucci.sampledata.sampleProducts
import br.com.alura.panucci.ui.screens.ProductDetailsScreen
import java.math.BigDecimal

fun NavGraphBuilder.productDetailsScreen(navController: NavHostController) {
    composable(
        "${AppDestination.ProductDetails.route}/{productId}?promoCode={promoCode}",
        arguments = listOf(
            navArgument("promoCode") { nullable = true }
        ),
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getString("productId")
        Log.i("MainActivity", "onCreate: id recebido ${id}")
        val promoCode = backStackEntry.arguments?.getString("promoCode")

        sampleProducts.find {
            it.id == id
        }?.let { product ->

            val discount = when (promoCode) {
                "ALURA" -> BigDecimal("0.1")
                else -> BigDecimal.ZERO
            }
            val currentPrice = product.price

            Log.i("MainActivity", "onCreate: product ${product} ")
            Log.i(
                "MainActivity",
                "onCreate: Calculo de desconto: ${currentPrice - (currentPrice * discount)} "
            )
            ProductDetailsScreen(
                product = product.copy(price = currentPrice - (currentPrice * discount)),
                onNavigateToCheckout = {
                    navController.navigate(AppDestination.Checkout.route)
                },
            )
            //caso o dado procurado na fonte de verdade seja nulo ...
//                                } ?: navController.popBackStack() //volta para a tela anterior
            //nao pode ser usado em composição
        }
            ?: LaunchedEffect(Unit) { navController.navigateUp() } //volta para a tela anterior porem possui integração com  deeplink

    }
}