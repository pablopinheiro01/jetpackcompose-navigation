package br.com.alura.panucci.navigation

sealed class AppDestination(val route: String){
    object HighLight: AppDestination("highlight")
    object Menu: AppDestination("menu")
    object Drinks: AppDestination("drinks")
    object ProductDetails: AppDestination("productDetails")
    object Checkout: AppDestination("checkout")
}