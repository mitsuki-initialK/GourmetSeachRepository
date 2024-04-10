package com.example.gourmetsearchapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gourmetsearchapp.model.Restaurant

@Composable
fun DetailScreen(
    restaurant: Restaurant,
    modifier :Modifier = Modifier
) {
    Column(modifier = modifier){
        Text(
            text = "${restaurant.name} "
        )
        Text(
            text = restaurant.access
        )
        Text(
            text = "${restaurant.address} "
        )
        Text(
            text = "${restaurant.open} "
        )

        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(restaurant.photo.mobile["l"])
                .crossfade(true)
                .build(),
            //error = painterResource(R.drawable.ic_broken_image),
            //placeholder = painterResource(R.drawable.loading_img),
            contentDescription = "restaurant logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
        )
    }

}




