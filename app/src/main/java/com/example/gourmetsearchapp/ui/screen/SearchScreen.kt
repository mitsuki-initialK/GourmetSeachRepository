package com.example.gourmetsearchapp.ui.screen

import android.location.Location
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gourmetsearchapp.R
import com.example.gourmetsearchapp.location.LocationRepository
import kotlinx.coroutines.flow.StateFlow


@Composable
fun SearchScreen(
    modifier : Modifier = Modifier,
    locationRepository: LocationRepository,
    onSearchButtonClick : (Location, SearchUiState) -> Unit,
    searchViewModel: SearchViewModel = viewModel(
        factory = viewModelFactory {
            initializer { SearchViewModel(locationRepository) }
        }
    )
) {

    val searchUiState by searchViewModel.uiState.collectAsState()
    val rangeOptions = listOf("300m", "500m", "1000m", "2000m", "3000m")

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.select_search_range),
            fontSize = 24.sp,
        )

        Spacer(modifier = Modifier.size(16.dp))

        Row{
            val color = if (searchUiState.getLocationState is GetLocationState.Success)
                Color(0, 0, 255, 128)
                else Color(255, 0, 0, 128)

            if(searchUiState.getLocationState is GetLocationState.Success){
                Icon(
                    painter = painterResource(id = R.drawable.ic_location_searching),
                    modifier = Modifier.size(20.dp),
                    tint = color,
                    contentDescription = ""
                )
            }else{
                IconButton(
                    onClick = searchViewModel::getLocation,
                    modifier = Modifier.size(20.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        tint = color,
                        contentDescription = ""
                    )
                }
            }

            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = when(val state = searchUiState.getLocationState){
                    is GetLocationState.Success -> stringResource(R.string.location) + state.addressLine
                    is GetLocationState.Loading -> stringResource(R.string.getting_location)
                    is GetLocationState.Error   -> stringResource(R.string.faild_to_get_location)
                } ,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .border(
                        BorderStroke(1.dp, color), // ボーダーの設定
                        shape = RoundedCornerShape(16.dp) // 半円の角を持つ形状を指定
                    )
            )
        }

        Spacer(modifier = Modifier.size(64.dp))

        SingleChoiceSegmentedButtonRow {
            rangeOptions.forEachIndexed {index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = rangeOptions.size
                    ),
                    onClick = {
                        searchViewModel.updateUiState(index)
                    },
                    selected = index == searchUiState.rangeNum,
                    label = { Text(label) }
                )
            }
        }

        Spacer(modifier = Modifier.size(64.dp))

        Button(
            onClick = {
                val state = searchUiState.getLocationState
                if(state is GetLocationState.Success) {
                    onSearchButtonClick(state.location, searchUiState)
                }
            },
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                modifier = Modifier.size(20.dp),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = stringResource(R.string.search_gourmet_current_condition),
                fontSize = 16.sp
            )
        }
    }
}

