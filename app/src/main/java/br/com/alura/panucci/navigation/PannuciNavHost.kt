package br.com.alura.panucci.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import br.com.alura.panucci.ui.components.BottomAppBarItem

@Composable
fun PanucciNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = highlightsListRoute,
    ) {
        //a navegacao serao composables injetados
        //dentro do slot de content configurado no APP
        highlightsListScreen(navController)
        menuScreen(navController)
        drinksScreen(navController)
        productDetailsScreen(navController)
        checkoutScreen(navController)
    }

}

fun NavController.navigateSingleTopWithPopUpTo(
    item: BottomAppBarItem
) {
    val (route, navigation) = when (item) {
        BottomAppBarItem.Drinks ->
            Pair(
                drinksRoute,
                ::navigateToDrinks //referencia para a rota
            )
        BottomAppBarItem.HighlightList ->
            Pair(
                highlightsListRoute,
                ::navigateToHighLightsList
            )
        BottomAppBarItem.Menu ->
            Pair(
                menuRoute,
                ::navigateToMenu
            )
    }

    //monta o navOptions inserindo a val route definida acima no Pair
    val navOptions = navOptions {
        launchSingleTop = true
        popUpTo(route)
    }
    //navega na rota passando os options configurados acima
    navigation(navOptions)
}
