package com.example.musicplayerandroidapp.ui.detail

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.musicplayerandroidapp.data.MusicRepository
import com.example.musicplayerandroidapp.model.MusicModel
import com.example.musicplayerandroidapp.model.PlayerState
import com.example.musicplayerandroidapp.service.MediaState
import com.example.musicplayerandroidapp.service.PlayMusicServiceHandler
import com.example.musicplayerandroidapp.service.PlayerEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: MusicRepository,
    private val mediaServiceHandler: PlayMusicServiceHandler,
): ViewModel() {

    private val id: Int = requireNotNull(savedStateHandle["id"])

    private var currentId: Int = -1

    private val allMusic = repository.getMusicList()

    private val _uiState = MutableStateFlow(UiState.initValue())
    val uiState = _uiState.asStateFlow()

    private val _playerUiState = MutableStateFlow(PlayerState.INITIAL)
    val playerUiState = _playerUiState.asStateFlow()

    var duration by savedStateHandle.saveable { mutableStateOf(0L) }
    var progress by savedStateHandle.saveable { mutableStateOf(0f) }
    var progressString by savedStateHandle.saveable { mutableStateOf("00:00") }
    var isPlaying by savedStateHandle.saveable { mutableStateOf(false) }

    init {
        _uiState.value = _uiState.value.copy(
            music = allMusic.find { it.id == id },
        )
        currentId = id
        handlePlayer()
        loadData()
    }

    fun onPlayButtonClicked() {
        viewModelScope.launch {
            mediaServiceHandler.onPlayerEvent(PlayerEvent.PlayPause)
        }
    }

    fun onNextButtonClicked() {
        val nextId = currentId + 1
        if (allMusic.any { it.id == nextId }) {
            currentId = nextId
            _uiState.value = _uiState.value.copy(
                music = allMusic.find { it.id == nextId }
            )
        } else {
            // error
        }
    }

    fun onPreviousButtonClicked() {
        val previousId = currentId - 1
        if (allMusic.any { it.id == previousId }) {
            currentId = previousId
            _uiState.value = _uiState.value.copy(
                music = allMusic.find { it.id == previousId }
            )
        } else {
            // error
        }
    }

    private fun loadData() {
        val mediaItem = MediaItem.Builder()
            .setUri("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setArtworkUri(Uri.parse("https://i.pinimg.com/736x/4b/02/1f/4b021f002b90ab163ef41aaaaa17c7a4.jpg"))
                    .setAlbumTitle("SoundHelix")
                    .setDisplayTitle("Song 1")
                    .build()
            ).build()
        mediaServiceHandler.addMediaItem(mediaItem)
    }

    private fun handlePlayer() {
        mediaServiceHandler.mediaState.onEach { mediaState ->
            when (mediaState) {
                is MediaState.Buffering -> calculateProgressValues(mediaState.progress)
                is MediaState.Initial -> {  }
                is MediaState.Playing -> isPlaying = mediaState.isPlaying
                is MediaState.Progress -> calculateProgressValues(mediaState.progress)
                is MediaState.Ready -> {
                    duration = mediaState.duration
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun calculateProgressValues(currentProgress: Long) {
        progress = if (currentProgress > 0) (currentProgress.toFloat() / duration) else 0f
        progressString = formatDuration(currentProgress)
    }

    private fun formatDuration(duration: Long): String {
        val minutes: Long = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val seconds: Long = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS)
                - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
        return String.format("%02d:%02d", minutes, seconds)
    }

    data class UiState(
        val music: MusicModel?,
    ) {
        val hasNext = music?.let { it.id != 1 } ?: false
        val hasPrevious = music?.let { it.id != 2 } ?: false
        companion object {
            fun initValue() = UiState(
                music = null,
            )
        }
    }

    data class PlayerUiState(
        val state: PlayerState,
    )

}