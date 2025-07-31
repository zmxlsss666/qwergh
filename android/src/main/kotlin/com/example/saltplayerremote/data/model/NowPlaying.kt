// shared/src/commonMain/kotlin/com/example/saltplayerremote/data/model/NowPlaying.kt
package com.example.saltplayerremote.data.model

import kotlinx.serialization.Serializable

@Serializable
data class NowPlaying(
    val status: String,
    val title: String,
    val artist: String,
    val album: String,
    val isPlaying: Boolean,
    val position: Long,
    val volume: Float,
    val timestamp: Long
)