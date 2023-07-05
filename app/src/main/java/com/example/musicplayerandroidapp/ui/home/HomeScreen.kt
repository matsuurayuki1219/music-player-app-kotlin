package com.example.musicplayerandroidapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.musicplayerandroidapp.model.MusicModel
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onItemClicked: (Int) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    if (state.musicList.isNotEmpty()) {
        HomeScreen(
            musicList = state.musicList,
            onItemClicked = onItemClicked,
        )
    }
}

@Composable
fun HomeScreen(
    musicList: List<MusicModel>,
    onItemClicked: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        items(musicList) { music ->
            MusicItem(
                id = music.id,
                artistName = music.artistName,
                musicName = music.musicName,
                onItemClicked = onItemClicked
            )
            Spacer(modifier = Modifier.padding(bottom = 16.dp))
        }
    }
}

@Composable
fun MusicItem(
    id: Int,
    artistName: String,
    musicName: String,
    onItemClicked: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Magenta)
            .padding(8.dp)
            .clickable { onItemClicked(id) },
    ) {
        Text(text = artistName, fontSize = 16.sp, textAlign = TextAlign.Left)
        Spacer(modifier = Modifier.padding(bottom = 8.dp))
        Text(text = musicName, fontSize = 16.sp, textAlign = TextAlign.Left)
        Spacer(modifier = Modifier.padding(bottom = 8.dp))
    }
}