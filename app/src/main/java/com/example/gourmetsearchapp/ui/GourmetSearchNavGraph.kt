package com.example.gourmetsearchapp.ui


import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gourmetsearchapp.R
import com.example.gourmetsearchapp.location.NetworkLocationRepository
import com.example.gourmetsearchapp.ui.screen.DetailScreen
import com.example.gourmetsearchapp.ui.screen.ResultScreen
import com.example.gourmetsearchapp.ui.screen.SearchScreen



@Composable
fun GourmetSearchNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination : String = GourmetSearchDestinations.SEARCH_ROUTE,
    navActions : GourmetSearchNavigationActions = remember(navController) {
        GourmetSearchNavigationActions(navController)
    }
) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route ?: startDestination

    Scaffold(
        topBar = {
            GourmetSearchAppBar(
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = {navController.navigateUp()}
            )
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {

            composable(route = GourmetSearchDestinations.SEARCH_ROUTE) {
                val context = LocalContext.current
                val locationRepository = NetworkLocationRepository(context as Activity)

                SearchScreen(
                    modifier = Modifier.fillMaxSize(),
                    locationRepository,
                    onSearchButtonClick = {
                        navActions.navigateToResult()
                    },
                )
            }

            composable(route = GourmetSearchDestinations.RESULT_ROUTE) {
                ResultScreen(
                    onShowDetailButtonClick = {
                        navActions.navigateToDetail(it.id)
                    } ,
                    modifier = Modifier.fillMaxSize()
                )
            }

            composable(route = GourmetSearchDestinations.DETAIL_ROUTE) {
                    DetailScreen(
                        modifier = Modifier.fillMaxSize()
                    )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GourmetSearchAppBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {

    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth().height(56.dp)) {
                if (canNavigateBack) {
                    IconButton(onClick = navigateUp, modifier = Modifier.fillMaxHeight()) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "backButton"
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.ic_restaurant),
                        modifier = Modifier.size(32.dp),
                        contentDescription = ""
                    )
                    Text(
                        text = "Restaurant Search",
                        fontSize = 28.sp,
                        color = Color.White
                    )
                }
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
    )
}


