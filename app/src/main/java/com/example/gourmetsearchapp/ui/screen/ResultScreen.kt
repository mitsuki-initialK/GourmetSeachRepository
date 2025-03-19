package com.example.gourmetsearchapp.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gourmetsearchapp.R
import com.example.gourmetsearchapp.GourmetSearch.Restaurant
import kotlin.math.min


@Composable
fun ResultScreen(
    searchGourmetState : SearchGourmetState,
    onShowDetailButtonClick : (Restaurant) -> Unit,
    retryAction : () -> Unit,
    modifier : Modifier = Modifier
) {
    Column(modifier = modifier) {
        when (searchGourmetState) {
            is SearchGourmetState.Loading -> LoadingScreen(modifier)
            is SearchGourmetState.Success -> SuccessScreen(
                searchGourmetState.restaurantList,
                onShowDetailButtonClick,
                modifier = Modifier.weight(1f)
            )
            is SearchGourmetState.Error -> ErrorScreen(retryAction, modifier)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SuccessScreen(
    restaurantList : List<Restaurant>,
    onShowDetailButtonClick : (Restaurant) -> Unit,
    modifier: Modifier = Modifier
) {
    val maxItemsPerPage = 10 //1ページに表示する数
    val pagerState = rememberPagerState(pageCount = { ((restaurantList.size - 1) / maxItemsPerPage) + 1 })

    Text(stringResource(R.string.success_gourmet_search, restaurantList.size))
    HorizontalPager(state = pagerState, modifier = modifier) { page ->
        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            itemsIndexed(restaurantList) { index, restaurant ->
                val startIndex = page * maxItemsPerPage
                val endIndex = startIndex + min( maxItemsPerPage - 1, restaurantList.size - startIndex)
                if(index in startIndex..endIndex){
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
    }

    if(pagerState.pageCount > 1){
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(16.dp)
                )
            }
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
            Box(modifier = Modifier
                .size(72.dp)
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

            Spacer(Modifier.width(16.dp))

            Column {
                Text(
                    text = restaurant.name,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(8.dp))
                Row{
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        modifier = Modifier.size(20.dp),
                        tint = Color(0, 0, 255, 128),
                        contentDescription = ""
                    )
                    Text(
                        text = restaurant.access,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(128.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.catch_error), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction, ) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.padding(180.dp),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = modifier.size(200.dp),
            painter = painterResource(id = R.drawable.loading_img),
            contentDescription = ""
        )
    }
}
