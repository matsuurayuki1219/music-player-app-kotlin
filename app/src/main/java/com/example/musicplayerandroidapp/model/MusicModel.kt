package com.example.musicplayerandroidapp.model

import androidx.annotation.RawRes
import java.io.File

data class MusicModel(
    val id: Int,
    val artistName: String,
    val musicName: String,
    @RawRes val audioRes: Int,
)