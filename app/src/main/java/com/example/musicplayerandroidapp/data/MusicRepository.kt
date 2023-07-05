package com.example.musicplayerandroidapp.data

import com.example.musicplayerandroidapp.R
import com.example.musicplayerandroidapp.model.MusicModel

class MusicRepository {

    fun getMusicList(): List<MusicModel> {
        return listOf(
            MusicModel(
                id = 1,
                artistName = "King Gnu",
                musicName = "一途",
                audioRes = R.raw.kinggnu,
            ),
            MusicModel(
                id = 2,
                artistName = "YOAKE",
                musicName = "ねぇ",
                audioRes = R.raw.yoake,
            ),
        )
    }

}