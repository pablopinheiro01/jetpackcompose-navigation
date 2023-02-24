package br.com.alura.panucci.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import androidx.navigation.navOptions
import br.com.alura.panucci.ui.components.BottomAppBarItem

internal const val homeGraphRoute = "home"


fun NavGraphBuilder.homeGraph(navController: NavHostController) {
    //grafo aninhado
    navigation(
        startDestination = highlightsListRoute,
        route = homeGraphRoute //grafo da home criado
    ) {
        //a navegacao serao composables injetados
        //dentro do slot de content configurado no APP
        highlightsListScreen(navController)
        menuScreen(navController)
        drinksScreen(navController)
    }
}

fun NavController.navigateToHomeGraph(){ //navegacao generica
    navigate(homeGraphRoute)
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