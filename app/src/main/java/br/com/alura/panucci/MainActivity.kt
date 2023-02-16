package br.com.alura.panucci

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.alura.panucci.sampledata.bottomAppBarItems
import br.com.alura.panucci.sampledata.sampleProductWithImage
import br.com.alura.panucci.sampledata.sampleProducts
import br.com.alura.panucci.ui.components.BottomAppBarItem
import br.com.alura.panucci.ui.components.PanucciBottomAppBar
import br.com.alura.panucci.ui.screens.*
import br.com.alura.panucci.ui.theme.PanucciTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            //controlador criado default
            val navController = rememberNavController()

//            LaunchedEffect(key1 = Unit ){
//                delay(3000L)
//                navController.navigate("menu")
//            }

            PanucciTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var selectedItem by remember {
                        val item = bottomAppBarItems.first()
                        mutableStateOf(item)
                    }
                    PanucciApp(
                        bottomAppBarItemSelected = selectedItem,
                        onBottomAppBarItemSelectedChange = {
                            selectedItem = it
                        },
                        onFabClick = {
                        }) {
                        NavHost(
                            navController = navController ,
                            startDestination = "home",
                        ){

                            //a navegacao serao composables injetados
                            //dentro do slot de content configurado no APP
                            composable("home"){
                                HighlightsListScreen(products = sampleProducts)

                                /*
                                Utilizamos o LaunchedEffect() para executar o código de navegação, pois é possível configurá-lo para executar apenas uma vez e evitar múltiplas execuções por conta da recomposição.
                                 Entretanto, já utilizamos o remember para fazer essa mesma tarefa em outras situações, então porque não utilizarmos novamente?
                                O remember é utilizado para realizar uma computação e devolver um valor que não seja Unit, ou seja, é possível utilizá-lo para fazer essa tarefa:

                                composable("home") {
                                    HighlightsListScreen(products = sampleProducts)
                                    remember {
                                        navController.navigate("menu")
                                    }
                                }
                                O AS vai apresentar o seguinte warning: “remember calls must not return Unit”, ou seja,
                                 remember não deve devolver Unit. Mesmo que rode e funcione, é considerado uma má prática, portanto, utilize as APIs de Effect para isso.
                                 */

                                //API que resolve os problemas relacionados a recomposição das telas
                                LaunchedEffect(key1 = Unit ){
                                    //executa uma unica vez e possui um escopo de Coroutine
                                    navController.navigate("menu")
                                }

                            }
                            composable("menu"){
                                MenuListScreen(products = sampleProducts)
                            }
                        }
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanucciApp(
    bottomAppBarItemSelected: BottomAppBarItem = bottomAppBarItems.first(),
    onBottomAppBarItemSelectedChange: (BottomAppBarItem) -> Unit = {},
    onFabClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Ristorante Panucci")
                },
            )
        },
        bottomBar = {
            PanucciBottomAppBar(
                item = bottomAppBarItemSelected,
                items = bottomAppBarItems,
                onItemChange = onBottomAppBarItemSelectedChange,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onFabClick
            ) {
                Icon(
                    Icons.Filled.PointOfSale,
                    contentDescription = null
                )
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