// shared/src/commonMain/kotlin/com/example/saltplayerremote/ui/screen/PlayerScreen.kt
package com.example.saltplayerremote.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.example.saltplayerremote.ui.viewmodel.PlayerViewModel
import coil.compose.rememberImagePainter

class PlayerScreen(private val ip: String) : Screen {
    @Composable
    override fun Content() {
        val viewModel: PlayerViewModel = getScreenModel { PlayerViewModel(ip, 35373) }
        val state by viewModel.state.collectAsState()
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Player") },
                    navigationIcon = {
                        IconButton(onClick = { viewModel.disconnect() }) {
                            Icon(Icons.Default.ArrowBack, "Back")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                state.songInfo?.let { info ->
                    Image(
                        painter = rememberImagePainter(info.pic),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(info.name, style = MaterialTheme.typography.titleLarge)
                    Text(info.artist, style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(16.dp))
                }
                state.nowPlaying?.let { np ->
                    Slider(
                        value = np.position.toFloat(),
                        onValueChange = {},
                        valueRange = 0f..240000f,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = { viewModel.previousTrack() }) {
                            Icon(Icons.Default.SkipPrevious, "Previous")
                        }
                        IconButton(onClick = { viewModel.togglePlayPause() }) {
                            Icon(
                                if (np.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                "Play/Pause"
                            )
                        }
                        IconButton(onClick = { viewModel.nextTrack() }) {
                            Icon(Icons.Default.SkipNext, "Next")
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = { viewModel.volumeDown() }) {
                            Icon(Icons.Default.VolumeDown, "Volume Down")
                        }
                        IconButton(onClick = { viewModel.toggleMute() }) {
                            Icon(Icons.Default.VolumeMute, "Mute")
                        }
                        IconButton(onClick = { viewModel.volumeUp() }) {
                            Icon(Icons.Default.VolumeUp, "Volume Up")
                        }
                    }
                }
                if (state.error != null) {
                    Text(state.error!!, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}