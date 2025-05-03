package com.example.gourmetsearchapp.ui

import androidx.navigation.NavHostController
import com.example.gourmetsearchapp.ui.GourmetSearchDestinationsArgs.RESTAURANT_ID_ARG
import com.example.gourmetsearchapp.ui.GourmetSearchScreens.DETAIL_SCREEN
import com.example.gourmetsearchapp.ui.GourmetSearchScreens.RESULT_SCREEN
import com.example.gourmetsearchapp.ui.GourmetSearchScreens.SEARCH_SCREEN


private object GourmetSearchScreens {
    const val SEARCH_SCREEN = "search"
    const val RESULT_SCREEN = "result"
    const val DETAIL_SCREEN = "detail"
}

private object GourmetSearchDestinationsArgs {
    const val RESTAURANT_ID_ARG = "restaurantId"
}

object GourmetSearchDestinations {
    const val SEARCH_ROUTE = SEARCH_SCREEN
    const val RESULT_ROUTE = RESULT_SCREEN
    const val DETAIL_ROUTE = "$DETAIL_SCREEN/{$RESTAURANT_ID_ARG}"
}

class GourmetSearchNavigationActions(private val navController: NavHostController){

    fun navigateToResult() {
        navController.navigate(GourmetSearchDestinations.RESULT_ROUTE)
    }

    fun navigateToDetail(restaurantId : String) {
        navController.navigate("$DETAIL_SCREEN/$restaurantId")
    }

}