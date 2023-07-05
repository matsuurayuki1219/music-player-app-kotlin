package com.example.musicplayerandroidapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayerandroidapp.data.MusicRepository
import com.example.musicplayerandroidapp.model.MusicModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: MusicRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState.initValue())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.value = _uiState.value.copy(
            musicList = repository.getMusicList(),
        )
    }

    data class UiState(
        val musicList: List<MusicModel>,
    ) {
        companion object {
            fun initValue() = UiState(
                musicList = emptyList(),
            )
        }
    }

}