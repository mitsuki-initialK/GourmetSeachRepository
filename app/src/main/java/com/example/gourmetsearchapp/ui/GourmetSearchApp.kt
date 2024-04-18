package com.example.gourmetsearchapp.ui

import androidx.annotation.StringRes
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gourmetsearchapp.R
import com.example.gourmetsearchapp.gourmetSearchAPI.Restaurant
import com.example.gourmetsearchapp.ui.screen.DetailScreen
import com.example.gourmetsearchapp.ui.screen.GourmetSearchViewModel
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
                            imageVector = Icons.Filled.ArrowBack,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GourmetSearchApp(
    gourmetSearchViewModel: GourmetSearchViewModel,
    getLocation : () -> Unit,
    navController: NavHostController = rememberNavController(),
) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = GourmetSearchScreen.valueOf(
        backStackEntry?.destination?.route ?: GourmetSearchScreen.Search.name
    )

    var selectedRestaurant by remember{ mutableStateOf<Restaurant?>(null)}

    var addressLine = gourmetSearchViewModel.addressLine.collectAsState()

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
            startDestination = GourmetSearchScreen.Search.name,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {

            composable(route = GourmetSearchScreen.Search.name) {
                SearchScreen(
                    locationAvailable = gourmetSearchViewModel.locationAvailable,
                    getLocation = getLocation,
                    address = addressLine.value,
                    onSearchButtonClick = {
                        if(gourmetSearchViewModel.locationAvailable){
                            gourmetSearchViewModel.rangeNum = it
                            gourmetSearchViewModel.getGourmetInfo()
                            navController.navigate(GourmetSearchScreen.Result.name)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = GourmetSearchScreen.Result.name) {
                ResultScreen(
                    searchGourmetState = gourmetSearchViewModel.searchGourmetState,
                    onShowDetailButtonClick = {
                        selectedRestaurant = it
                        navController.navigate(GourmetSearchScreen.Detail.name)
                    } ,
                    retryAction = gourmetSearchViewModel::getGourmetInfo,
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


