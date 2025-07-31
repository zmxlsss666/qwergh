// shared/src/commonMain/kotlin/com/example/saltplayerremote/data/model/SongInfo.kt
package com.example.saltplayerremote.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SongInfo(
    val name: String,
    val artist: String,
    val url: String,
    val pic: String,
    val lrc: String
)