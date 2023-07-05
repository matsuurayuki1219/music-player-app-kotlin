package com.example.musicplayerandroidapp.model

import java.io.File

data class MusicModel(
    val artistName: String,
    val musicName: String,
    val audioFile: File,
)