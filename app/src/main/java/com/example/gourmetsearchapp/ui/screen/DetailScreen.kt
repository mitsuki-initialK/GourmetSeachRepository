package com.example.gourmetsearchapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gourmetsearchapp.R
import com.example.gourmetsearchapp.gourmetSearchAPI.Restaurant

@Composable
fun DetailScreen(
    restaurant: Restaurant,
    modifier :Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)){
        Text(
            text = "${restaurant.name} ",
            fontSize = 32.sp,
            textAlign = TextAlign.Center
        )
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = Color.Blue,
            thickness = 1.dp
        )
        Spacer(Modifier.height(16.dp))
        Row{
            Text(text = "住　　所：")
            Text(text = "${restaurant.address} ")
        }
        Spacer(Modifier.height(8.dp))
        Row{
            Text(text = "アクセス：")
            Text(text = "${restaurant.access} ")
        }
        Spacer(Modifier.height(8.dp))
        Row{
            Text(text = "営業時間：")
            Text(text = "${restaurant.open} ")
        }
        Spacer(Modifier.height(16.dp))
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(restaurant.photo.mobile["l"])
                .crossfade(true)
                .build(),
            error = painterResource(R.drawable.ic_broken_image),
            placeholder = painterResource(R.drawable.loading_img),
            contentDescription = "restaurant logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
        )
    }

}




