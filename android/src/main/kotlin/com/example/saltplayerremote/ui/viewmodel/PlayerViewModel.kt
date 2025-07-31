// shared/src/commonMain/kotlin/com/example/saltplayerremote/ui/viewmodel/PlayerViewModel.kt
package com.example.saltplayerremote.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saltplayerremote.data.repository.SaltApiRepository
import com.example.saltplayerremote.di.AppModule
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

data class PlayerState(
    val nowPlaying: com.example.saltplayerremote.data.model.NowPlaying? = null,
    val songInfo: com.example.saltplayerremote.data.model.SongInfo? = null,
    val error: String? = null
)

class PlayerViewModel(
    private val ip: String,
    private val port: Int,
    private val repo: SaltApiRepository = AppModule.saltApiRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PlayerState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                refresh()
                delay(2000)
            }
        }
    }

    suspend fun refresh() {
        val res = repo.getNowPlaying(ip, port)
        if (res.isSuccess) {
            val np = res.getOrNull()!!
            _state.value = _state.value.copy(nowPlaying = np, error = null)
            if (np.title.isNotEmpty()) {
                val songRes = repo.searchSong(np.title, np.artist)
                if (songRes.isSuccess) {
                    _state.value = _state.value.copy(songInfo = songRes.getOrNull()?.firstOrNull())
                }
            }
        } else {
            _state.value = _state.value.copy(error = res.exceptionOrNull()?.message ?: "Error")
        }
    }

    fun togglePlayPause() {
        viewModelScope.launch {
            repo.togglePlayPause(ip, port)
            refresh()
        }
    }

    fun nextTrack() {
        viewModelScope.launch {
            repo.nextTrack(ip, port)
            refresh()
        }
    }

    fun previousTrack() {
        viewModelScope.launch {
            repo.previousTrack(ip, port)
            refresh()
        }
    }

    fun volumeUp() {
        viewModelScope.launch {
            repo.volumeUp(ip, port)
            refresh()
        }
    }

    fun volumeDown() {
        viewModelScope.launch {
            repo.volumeDown(ip, port)
            refresh()
        }
    }

    fun toggleMute() {
        viewModelScope.launch {
            repo.toggleMute(ip, port)
            refresh()
        }
    }

    fun disconnect() {
        viewModelScope.cancel()
    }
}