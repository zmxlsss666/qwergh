// shared/src/commonMain/kotlin/com/example/saltplayerremote/data/model/NeteaseSearchResponse.kt
package com.example.saltplayerremote.data.model

import kotlinx.serialization.Serializable

@Serializable
data class NeteaseSearchResponse(
    val result: NeteaseSearchResult,
    val code: Int
)

@Serializable
data class NeteaseSearchResult(
    val songs: List<NeteaseSong>,
    val hasMore: Boolean,
    val songCount: Int
)

@Serializable
data class NeteaseSong(
    val id: Long,
    val name: String,
    val artists: List<NeteaseArtist>
)

@Serializable
data class NeteaseArtist(
    val name: String
)