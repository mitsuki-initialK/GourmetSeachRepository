package com.example.gourmetsearchapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gourmetsearchapp.R
import com.example.gourmetsearchapp.gourmetSearch.Device
import com.example.gourmetsearchapp.gourmetSearch.Restaurant

@Composable
fun DetailScreen(
    modifier :Modifier = Modifier,
    detailViewModel: DetailViewModel = hiltViewModel(),
) {

    val restaurant = detailViewModel.restaurant

    Column(modifier = modifier.padding(16.dp)){
        Text(
            text = "${restaurant.name} ",
            fontSize = 32.sp,
            maxLines = 1,
            overflow = TextOverflow.Visible,
            textAlign = TextAlign.Center
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color.Blue
        )
        Spacer(Modifier.height(16.dp))
        Row{
            Text(text = stringResource(R.string.address))
            Text(text = "${restaurant.address} ")
        }
        Spacer(Modifier.height(8.dp))
        Row{
            Text(text = stringResource(R.string.access))
            Text(text = "${restaurant.access} ")
        }
        Spacer(Modifier.height(8.dp))
        Row{
            Text(text = stringResource(R.string.opening_hours))
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

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview(){
    val restaurant = Restaurant(
        id = "0000000",
        name = "鶏あえず 蒲田店",
        address = "東京都大田区西蒲田７-3-3 8F",
        access = "JR 京浜東北線 蒲田駅 徒歩1分（蒲田駅西口ドン・キホーテ8F",
        open = "月～木: 14:00～翌2:00 （料理L.O. 翌1:00 ドリンクL.O. 翌1:30）金、土: 12:00～翌2:00 （料理L.O. 翌1:00 ドリンクL.O. 翌1:30)",
        logo = "https://imgfp.hotp.jp/IMGH/13/19/P044811319/P044811319_69.jpg",
        lat = 35.5632495372,
        lng = 139.7156569299,
        photo = Device(
            pc = emptyMap(),
            mobile = emptyMap()
        ),
        distance = 500,
    )
    DetailScreen()
}




