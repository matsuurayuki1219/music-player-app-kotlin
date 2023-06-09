package com.example.musicplayerandroidapp.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.musicplayerandroidapp.R
import com.example.musicplayerandroidapp.model.MusicModel
import com.example.musicplayerandroidapp.model.PlayerState

@Composable
fun DetailScreen(
    viewModel: DetailViewModel = hiltViewModel(),
    startService: () -> Unit,
    stopService: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val playerState by viewModel.playerUiState.collectAsStateWithLifecycle()
    DetailScreen(
        music = state.music,
        hasNext = state.hasNext,
        hasPrevious = state.hasPrevious,
        position = playerState.progress,
        duration = playerState.duration,
        onPlayButtonClicked = { viewModel.onPlayButtonClicked() },
        onNextButtonClicked = { viewModel.onNextButtonClicked() },
        onPreviousButtonClicked = { viewModel.onPreviousButtonClicked() },
        onSeekTo = { viewModel.onSeekTo(position = it) }
    )
    when (playerState.state) {
        PlayerState.INITIAL -> {}
        PlayerState.READY -> {
            LaunchedEffect(true) {
                startService()
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            stopService()
        }
    }
}

@Composable
fun DetailScreen(
    music: MusicModel?,
    hasNext: Boolean,
    hasPrevious: Boolean,
    position: Long?,
    duration: Long?,
    onPlayButtonClicked: (Unit) -> Unit,
    onNextButtonClicked: (Unit) -> Unit,
    onPreviousButtonClicked: (Unit) -> Unit,
    onSeekTo: (Long) -> Unit,
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
            PlayerSeekBar(position = position, duration = duration, onSeekTo = onSeekTo)
            Spacer(modifier = Modifier.padding(top = 30.dp))
            PlayerButton(
                hasNext = hasNext,
                hasPrevious = hasPrevious,
                onPreviousButtonClicked = onPreviousButtonClicked,
                onNextButtonClicked = onNextButtonClicked,
                onPlayButtonClicked = onPlayButtonClicked,
            )
        }
    }
}

@Composable
fun PlayerSeekBar(
    position: Long?,
    duration: Long?,
    onSeekTo: (Long) -> Unit,
) {
    val value = if (position == null || duration == null) 0f else (position / duration).toFloat()
    Slider(value = value, onValueChange = { onSeekTo(it.toLong()) })
}

@Composable
fun PlayerButton(
    hasNext: Boolean,
    hasPrevious: Boolean,
    onPlayButtonClicked: (Unit) -> Unit,
    onNextButtonClicked: (Unit) -> Unit,
    onPreviousButtonClicked: (Unit) -> Unit,
) {
    Row {
        Image(
            painter = painterResource(id = R.drawable.ic_skip_previous),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .alpha(if (hasNext) 1f else 0.4f)
                .clickable { if (hasNext) onPreviousButtonClicked.invoke(Unit) },
        )
        Image(
            painter = painterResource(id = R.drawable.ic_play),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .clickable { onPlayButtonClicked.invoke(Unit) },
        )
        Image(
            painter = painterResource(id = R.drawable.ic_skip_next),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .alpha(if (hasPrevious) 1f else 0.4f)
                .clickable { if (hasPrevious) onNextButtonClicked.invoke(Unit) }
        )
    }
}