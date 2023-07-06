package com.example.musicplayerandroidapp.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.musicplayerandroidapp.R
import com.example.musicplayerandroidapp.model.MusicModel

@Composable
fun DetailScreen(
    viewModel: DetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    DetailScreen(
        music = state.music,
    )
}

@Composable
fun DetailScreen(
    music: MusicModel?,
) {
    music?.let {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.padding(top = 100.dp))
            Text(text = it.artistName, fontSize = 24.sp, textAlign = TextAlign.Left)
            Spacer(modifier = Modifier.padding(top = 16.dp))
            Text(text = it.musicName, fontSize = 24.sp, textAlign = TextAlign.Left)
            Spacer(modifier = Modifier.padding(top = 100.dp))
            PlayerButton()
        }
    }
}

@Composable
fun PlayerButton() {
    Row {
        Image(
            painter = painterResource(id = R.drawable.ic_skip_previous),
            contentDescription = null,
            modifier = Modifier.weight(1f)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_play),
            contentDescription = null,
            modifier = Modifier.weight(1f)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_skip_next),
            contentDescription = null,
            modifier = Modifier.weight(1f)
        )
    }
}