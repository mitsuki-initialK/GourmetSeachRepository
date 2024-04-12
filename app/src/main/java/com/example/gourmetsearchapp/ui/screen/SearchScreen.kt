package com.example.gourmetsearchapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun SearchScreen(
    onSearchButtonClick : (Int) -> Unit,
    modifier : Modifier = Modifier
) {

    val radioOptions = listOf("300m", "500m", "1000m", "2000m", "3000m")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0] ) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "検索範囲を選択してください",
        )
        radioOptions.forEach { text ->
            Row(
                Modifier.fillMaxWidth()
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) }
                    )
                    .padding(horizontal = 16.dp)
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
        Button(
            onClick = { onSearchButtonClick(radioOptions.indexOf(selectedOption) + 1) }
        ) {
            Text(
                text = "現在の条件で検索",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    SearchScreen(
        modifier = Modifier.fillMaxSize(),
        onSearchButtonClick = {}
    )
}

