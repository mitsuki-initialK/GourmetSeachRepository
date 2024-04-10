package com.example.gourmetsearchapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gourmetsearchapp.model.GourmetSearchUiState
import com.example.gourmetsearchapp.model.Restaurant


@Composable
fun ResultScreen(
    gourmetSearchUiState : GourmetSearchUiState,
    onShowDetailButtonClick : (Restaurant) -> Unit,
    modifier : Modifier = Modifier
) {
    Column(modifier = modifier) {
        when (gourmetSearchUiState) {
            is GourmetSearchUiState.Loading -> LoadingScreen()
            is GourmetSearchUiState.Success -> SuccessScreen(
                gourmetSearchUiState.restaurantList,
                onShowDetailButtonClick,
                modifier = Modifier.weight(1f)
            )
            is GourmetSearchUiState.Error -> ErrorScreen()
        }
    }
}

@Composable
fun ErrorScreen() {
    Text(
        text = "ERROR"
    )
}

@Composable
fun LoadingScreen() {
    Text(
        text = "LORDING..."
    )
}

@Composable
fun SuccessScreen(
    restaurantList : List<Restaurant>,
    onShowDetailButtonClick : (Restaurant) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(modifier = modifier) {
        items(restaurantList) { restaurant ->
            RestaurantCard(
                restaurant = restaurant,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clickable { onShowDetailButtonClick(restaurant) }
            )
        }
    }
}

@Composable
fun RestaurantCard(
    restaurant: Restaurant,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier){
        Row(modifier = Modifier.padding(8.dp)){
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(restaurant.logo)
                    .crossfade(true)
                    .build(),
                //error = painterResource(R.drawable.ic_broken_image),
                //placeholder = painterResource(R.drawable.loading_img),
                contentDescription = "restaurant logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(CircleShape)
                    .fillMaxHeight(),
            )

            Column {
                Text(
                    text = restaurant.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = restaurant.access,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}


