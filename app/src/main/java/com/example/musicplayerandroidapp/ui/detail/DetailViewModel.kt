package com.example.musicplayerandroidapp.ui.detail

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import com.example.musicplayerandroidapp.R
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

    private val _playerUiState = MutableStateFlow(PlayerUiState.initValue())
    val playerUiState = _playerUiState.asStateFlow()

    init {
        _uiState.value = _uiState.value.copy(
            music = allMusic.find { it.id == id },
        )
        currentId = id
        handlePlayer()
        prepareAudio()
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

    fun onSeekTo(position: Long) {
        val playerState = _playerUiState.value
        if (playerState.duration == null || playerState.progress == null) return
        val duration = playerState.duration
        val progress = position / duration
        viewModelScope.launch {
            mediaServiceHandler.onPlayerEvent(PlayerEvent.UpdateProgress(newProgress = progress.toFloat()))
        }
    }

    @UnstableApi
    private fun prepareAudio() {
        val music = allMusic.find { it.id == currentId } ?: return
        val mediaItem = MediaItem.Builder()
            .setUri(RawResourceDataSource.buildRawResourceUri(music.audioRes))
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setArtworkUri(Uri.parse("https://i.pinimg.com/736x/4b/02/1f/4b021f002b90ab163ef41aaaaa17c7a4.jpg"))
                    .setAlbumTitle(music.artistName)
                    .setDisplayTitle(music.musicName)
                    .build()
            ).build()
        mediaServiceHandler.playAudio(mediaItem)
    }

    private fun handlePlayer() {
        mediaServiceHandler.mediaState.onEach { mediaState ->
            when (mediaState) {
                is MediaState.Buffering -> {
                    calculateProgressValues(mediaState.progress)
                }
                is MediaState.Initial -> {
                    _playerUiState.value = _playerUiState.value.copy(
                        state = PlayerState.INITIAL,
                    )
                }
                is MediaState.Playing -> {
                    _playerUiState.value = _playerUiState.value.copy(
                        isPlaying = mediaState.isPlaying,
                    )
                }
                is MediaState.Progress -> {
                    calculateProgressValues(mediaState.progress)
                }
                is MediaState.Ready -> {
                    _playerUiState.value = _playerUiState.value.copy(
                        duration = mediaState.duration,
                        state = PlayerState.READY,
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun calculateProgressValues(currentProgress: Long) {
        val duration =_playerUiState.value.duration ?: return
        if (duration <= 0) return
        _playerUiState.value = _playerUiState.value.copy(
            progress = if (currentProgress > 0) (currentProgress / duration) else 0L
        )
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
        val progress: Long?,
        val duration: Long?,
        val isPlaying: Boolean,
        val state: PlayerState,
    ) {
        companion object {
            fun initValue() = PlayerUiState(
                state = PlayerState.INITIAL,
                progress = null,
                duration = null,
                isPlaying = false,
            )
        }
    }

}