package com.example.gourmetsearchapp.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.gourmetsearchapp.R


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SearchScreen(
    getLocationState: GetLocationState,
    retryGetLocation : () -> Unit,
    onSearchButtonClick : (Int) -> Unit,
    modifier : Modifier = Modifier,
) {

    val radioOptions = listOf("300m", "500m", "1000m", "2000m", "3000m")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0] ) }

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
            val color = if (getLocationState is GetLocationState.Success)
                Color(0, 0, 255, 128)
                else Color(255, 0, 0, 128)

            if(getLocationState is GetLocationState.Success){
                Icon(
                    painter = painterResource(id = R.drawable.ic_location_searching),
                    modifier = Modifier.size(20.dp),
                    tint = color,
                    contentDescription = ""
                )
            }else{
                IconButton(
                    onClick = retryGetLocation,
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
                text = when(getLocationState){
                    is GetLocationState.Success -> stringResource(R.string.location) + getLocationState.addressLine
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

        radioOptions.forEach { text ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) }
                    )
                    .padding(horizontal = 16.dp),
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = { onOptionSelected(text) }
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.size(64.dp))

        Button(
            onClick = {
                if(getLocationState is GetLocationState.Success) {
                    onSearchButtonClick(radioOptions.indexOf(selectedOption) + 1)
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
