package com.example.gourmetsearchapp.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gourmetsearchapp.model.GourmetSearchViewModel
import com.example.gourmetsearchapp.model.Restaurant
import com.example.gourmetsearchapp.ui.screen.DetailScreen
import com.example.gourmetsearchapp.ui.screen.ResultScreen
import com.example.gourmetsearchapp.ui.screen.SearchScreen

enum class GourmetSearchScreen(@StringRes val title : Int) {
    Search(title = 1),
    Result(title = 2),
    Detail(title = 3),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GourmetSearchAppBar(
    currentScreen: GourmetSearchScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {

    TopAppBar(
        title = { "TOP APP BAR" },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "backButton"
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GourmetSearchApp(
    gourmetSearchViewModel: GourmetSearchViewModel,
    navController: NavHostController = rememberNavController()
) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = GourmetSearchScreen.valueOf(
        backStackEntry?.destination?.route ?: GourmetSearchScreen.Search.name
    )

    var selectedRestaurant by remember{ mutableStateOf<Restaurant?>(null)}

    Scaffold(
        topBar = {
            GourmetSearchAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = {navController.navigateUp()}
            )
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = GourmetSearchScreen.Search.name,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {

            composable(route = GourmetSearchScreen.Search.name) {
                SearchScreen(
                    onSearchButtonClick = {
                        gourmetSearchViewModel.rangeNum = it
                        gourmetSearchViewModel.getGourmetInfo()
                        navController.navigate(GourmetSearchScreen.Result.name)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = GourmetSearchScreen.Result.name) {
                ResultScreen(
                    gourmetSearchUiState = gourmetSearchViewModel.gourmetSearchUiState,
                    onShowDetailButtonClick = {
                        selectedRestaurant = it
                        navController.navigate(GourmetSearchScreen.Detail.name)
                    } ,
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = GourmetSearchScreen.Detail.name) {
                selectedRestaurant?.let { restaurant ->
                    DetailScreen(
                        restaurant,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}


