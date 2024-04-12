package com.example.gourmetsearchapp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gourmetsearchapp.R
import com.example.gourmetsearchapp.model.GourmetSearchUiState
import com.example.gourmetsearchapp.model.Restaurant


@Composable
fun ResultScreen(
    gourmetSearchUiState : GourmetSearchUiState,
    onShowDetailButtonClick : (Restaurant) -> Unit,
    retryAction : () -> Unit,
    modifier : Modifier = Modifier
) {
    Column(modifier = modifier) {
        when (gourmetSearchUiState) {
            is GourmetSearchUiState.Loading -> LoadingScreen(modifier)
            is GourmetSearchUiState.Success -> SuccessScreen(
                gourmetSearchUiState.restaurantList,
                onShowDetailButtonClick,
                modifier = Modifier.weight(1f)
            )
            is GourmetSearchUiState.Error -> ErrorScreen(retryAction, modifier)
        }
    }
}

@Composable
fun SuccessScreen(
    restaurantList : List<Restaurant>,
    onShowDetailButtonClick : (Restaurant) -> Unit,
    modifier: Modifier = Modifier
) {
    Text("${restaurantList.size}件見つかりました（最大100件まで）")
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
            Box(modifier = Modifier.size(48.dp)
                .clip(RoundedCornerShape(8.dp))
            ){
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(restaurant.logo)
                        .crossfade(true)
                        .build(),
                    error = painterResource(R.drawable.ic_broken_image),
                    placeholder = painterResource(R.drawable.loading_img),
                    contentDescription = "restaurant logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(Modifier.width(8.dp))

            Column {
                Text(
                    text = restaurant.name,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = restaurant.access,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = "エラーが発生しました", modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text("再試行")
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(id = R.drawable.loading_img),
        contentDescription = ""
    )
}


