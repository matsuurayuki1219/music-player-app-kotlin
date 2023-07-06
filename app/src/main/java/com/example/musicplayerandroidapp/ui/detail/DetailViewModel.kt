package com.example.musicplayerandroidapp.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.musicplayerandroidapp.data.MusicRepository
import com.example.musicplayerandroidapp.model.MusicModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: MusicRepository,
): ViewModel() {

    private val id: Int = requireNotNull(savedStateHandle["id"])

    private val allMusic = repository.getMusicList()

    private val _uiState = MutableStateFlow(UiState.initValue())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.value = _uiState.value.copy(
            music = allMusic.find { it.id == id }
        )
    }

    data class UiState(
        val music: MusicModel?,
    ) {
        companion object {
            fun initValue() = UiState(
                music = null,
            )
        }
    }

}