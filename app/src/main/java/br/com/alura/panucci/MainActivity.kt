package br.com.alura.panucci

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.alura.panucci.navigation.AppDestination
import br.com.alura.panucci.sampledata.bottomAppBarItems
import br.com.alura.panucci.sampledata.sampleProducts
import br.com.alura.panucci.ui.components.BottomAppBarItem
import br.com.alura.panucci.ui.components.PanucciBottomAppBar
import br.com.alura.panucci.ui.screens.*
import br.com.alura.panucci.ui.theme.PanucciTheme
import java.math.BigDecimal

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            //controlador criado default
            val navController = rememberNavController()

            logBackStackNavigation(navController)

            val backStackEntryState by navController.currentBackStackEntryAsState()
            val currentDestination = backStackEntryState?.destination


            PanucciTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var selectedItem by remember(currentDestination) {

                        val item = currentDestination?.let {
                            bottomAppBarItems.find { app ->
                                it.route == app.destination.route
                            }
                        } ?: bottomAppBarItems.first()

                        mutableStateOf(item)
                    }

                    //verifica se o destino contem appBar
                    val containsInBottomAppBarItems = currentDestination?.let { destination ->
                        bottomAppBarItems.find {
                            it.destination.route == destination.route
                        }
                    } != null

                    //verifica se os destinos contem FAB
                    val isShowFab = when (currentDestination?.route) {
                        AppDestination.Menu.route,
                        AppDestination.Drinks.route -> true
                        else -> false
                    }

                    PanucciApp(
                        bottomAppBarItemSelected = selectedItem,
                        onBottomAppBarItemSelectedChange = {
                            selectedItem = it
                            //utiliza um evento interno do composable que utiliza uma API de effect por baixo dos panos
                            val route = it.destination.route
                            navController.navigate(route) {
                                launchSingleTop = true //nao recarrega a tela
                                popUpTo(route) // remove a screen da stack
                            }
                        },
                        onFabClick = {
                            navController.navigate(AppDestination.Checkout.route)
                        },
                        isShowTopBar = containsInBottomAppBarItems,
                        isShowBottomBar = containsInBottomAppBarItems,
                        isShowFab = isShowFab

                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = AppDestination.HighLight.route,
                        ) {

                            //a navegacao serao composables injetados
                            //dentro do slot de content configurado no APP
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
                            composable(AppDestination.Menu.route) {
                                MenuListScreen(
                                    products = sampleProducts,
                                    onNavigateToDetails = { productReceived ->
                                        navController.navigate("${AppDestination.ProductDetails.route}/${productReceived.id}")
                                    },

                                    )
                            }
                            composable(AppDestination.Drinks.route) {
                                DrinksListScreen(
                                    products = sampleProducts,
                                    onNavigateToDetails = { productReceived ->
                                        navController.navigate("${AppDestination.ProductDetails.route}/${productReceived.id}")
                                    },
                                )
                            }
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

                                    val discount = when(promoCode){
                                        "ALURA" -> BigDecimal("0.1")
                                        else -> BigDecimal.ZERO
                                    }
                                    val currentPrice = product.price

                                    Log.i("MainActivity", "onCreate: product ${product} ")
                                    Log.i("MainActivity", "onCreate: Calculo de desconto: ${currentPrice - (currentPrice*discount)} ")
                                    ProductDetailsScreen(
                                        product = product.copy(price = currentPrice - (currentPrice*discount)),
                                        onNavigateToCheckout = {
                                            navController.navigate(AppDestination.Checkout.route)
                                        },
                                    )
                                }

                            }
                            composable(AppDestination.Checkout.route) {
                                CheckoutScreen(products = sampleProducts)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun logBackStackNavigation(navController: NavHostController) {
        LaunchedEffect(Unit) {
            //codigo para analisar a backstack do navigation
            navController.addOnDestinationChangedListener(
                NavController.OnDestinationChangedListener { _, _, _ ->
                    val routes = navController.backQueue.map {
                        it.destination.route
                    }
                    Log.i("MainActivity", "backstack rotas $routes")

                })
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanucciApp(
    bottomAppBarItemSelected: BottomAppBarItem = bottomAppBarItems.first(),
    onBottomAppBarItemSelectedChange: (BottomAppBarItem) -> Unit = {},
    onFabClick: () -> Unit = {},
    isShowTopBar: Boolean = false,
    isShowBottomBar: Boolean = false,
    isShowFab: Boolean = false,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            if (isShowTopBar) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "Ristorante Panucci")
                    },
                )
            }
        },
        bottomBar = {
            if (isShowBottomBar) {
                PanucciBottomAppBar(
                    item = bottomAppBarItemSelected,
                    items = bottomAppBarItems,
                    onItemChange = onBottomAppBarItemSelectedChange,
                )
            }
        },
        floatingActionButton = {
            if (isShowFab) {
                FloatingActionButton(
                    onClick = onFabClick
                ) {
                    Icon(
                        Icons.Filled.PointOfSale,
                        contentDescription = null
                    )
                }
            }
        }
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun PanucciAppPreview() {
    PanucciTheme {
        Surface {
            PanucciApp {}
        }
    }
}