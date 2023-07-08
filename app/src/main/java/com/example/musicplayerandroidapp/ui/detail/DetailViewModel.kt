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

    private var currentId: Int = -1

    private val allMusic = repository.getMusicList()

    private val _uiState = MutableStateFlow(UiState.initValue())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.value = _uiState.value.copy(music = allMusic.find { it.id == id })
        currentId = id
    }

    fun onPlayButtonClicked() {}

    fun onNextButtonClicked() {}

    fun onPreviousButtonClicked() {}


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

}