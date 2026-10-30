package com.example.explora2.ui.screens.inicio

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.explora2.navigation.Screen
import com.example.explora2.ui.utils.calcularTamanoVentana
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // 1. Invocación de la utilidad WindowSizeClass (Guía 9)
    val tamanoVentana = calcularTamanoVentana()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Menú Explora2",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                NavigationDrawerItem(
                    label = { Text("Cambiar de Perfil") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.SelectorPerfil.route)
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Explora2 - Apoyo Didáctico") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir Menú")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                // 2. Selección dinámica según el ancho de la pantalla
                when (tamanoVentana.widthSizeClass) {
                    WindowWidthSizeClass.Compact -> {
                        HomeScreenCompact(
                            onComenzarClick = { navController.navigate(Screen.SelectorPerfil.route) }
                        )
                    }
                    WindowWidthSizeClass.Medium -> {
                        HomeScreenMedium(
                            onComenzarClick = { navController.navigate(Screen.SelectorPerfil.route) }
                        )
                    }
                    WindowWidthSizeClass.Expanded -> {
                        HomeScreenExpanded(
                            onComenzarClick = { navController.navigate(Screen.SelectorPerfil.route) }
                        )
                    }
                    else -> {
                        HomeScreenCompact(
                            onComenzarClick = { navController.navigate(Screen.SelectorPerfil.route) }
                        )
                    }
                }
            }
        }
    }
}