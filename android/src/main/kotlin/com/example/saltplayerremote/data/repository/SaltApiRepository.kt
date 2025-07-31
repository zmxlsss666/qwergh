// shared/src/commonMain/kotlin/com/example/saltplayerremote/data/repository/SaltApiRepository.kt
package com.example.saltplayerremote.data.repository

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.json.Json
import com.example.saltplayerremote.data.model.NowPlaying
import com.example.saltplayerremote.data.model.SongInfo
import com.example.saltplayerremote.data.model.NeteaseSearchResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

interface SaltApiRepository {
    suspend fun getNowPlaying(ip: String, port: Int): Result<NowPlaying>
    suspend fun togglePlayPause(ip: String, port: Int): Result<Unit>
    suspend fun nextTrack(ip: String, port: Int): Result<Unit>
    suspend fun previousTrack(ip: String, port: Int): Result<Unit>
    suspend fun volumeUp(ip: String, port: Int): Result<Unit>
    suspend fun volumeDown(ip: String, port: Int): Result<Unit>
    suspend fun toggleMute(ip: String, port: Int): Result<Unit>
    suspend fun searchSong(title: String, artist: String): Result<List<SongInfo>>
}

class SaltApiRepositoryImpl(
    private val client: HttpClient
) : SaltApiRepository {

    override suspend fun getNowPlaying(ip: String, port: Int): Result<NowPlaying> {
        return try {
            val url = "http://$ip:$port/api/now-playing"
            val response: NowPlaying = client.get(url).body()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun togglePlayPause(ip: String, port: Int): Result<Unit> {
        return try {
            val url = "http://$ip:$port/api/play-pause"
            client.get(url)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun nextTrack(ip: String, port: Int): Result<Unit> {
        return try {
            val url = "http://$ip:$port/api/next-track"
            client.get(url)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun previousTrack(ip: String, port: Int): Result<Unit> {
        return try {
            val url = "http://$ip:$port/api/previous-track"
            client.get(url)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun volumeUp(ip: String, port: Int): Result<Unit> {
        return try {
            val url = "http://$ip:$port/api/volume/up"
            client.get(url)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun volumeDown(ip: String, port: Int): Result<Unit> {
        return try {
            val url = "http://$ip:$port/api/volume/down"
            client.get(url)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleMute(ip: String, port: Int): Result<Unit> {
        return try {
            val url = "http://$ip:$port/api/mute"
            client.get(url)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchSong(title: String, artist: String): Result<List<SongInfo>> {
        return try {
            val query = "$title $artist"
            val url = "https://music.163.com/api/search/get?type=1&offset=0&limit=1&s=$query"
            val response: String = client.get(url).body()
            val searchResponse = Json.decodeFromString<NeteaseSearchResponse>(response)
            if (searchResponse.result.songs.isEmpty()) return Result.success(emptyList())
            val songId = searchResponse.result.songs.first().id
            val detailUrl = "https://api.injahow.cn/meting/?type=song&id=$songId"
            val detail: String = client.get(detailUrl).body()
            val type = object : TypeToken<List<SongInfo>>() {}.type
            val list = Gson().fromJson<List<SongInfo>>(detail, type)
            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}