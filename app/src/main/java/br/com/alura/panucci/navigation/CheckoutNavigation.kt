package br.com.alura.panucci.navigation

import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.alura.panucci.sampledata.sampleProducts
import br.com.alura.panucci.ui.screens.CheckoutScreen

fun NavGraphBuilder.checkoutScreen(navController: NavHostController) {
    composable(AppDestination.Checkout.route) {
        CheckoutScreen(products = sampleProducts,
            onPopBackStack = {
                Log.i("MainActivity", "onCreate: onPopBackStack called")
//                                        navController.navigateUp()
                navController.navigate(AppDestination.HighLight.route) {
                    popUpTo(AppDestination.Checkout.route)
                }
            }
        )
    }
}