// shared/src/commonMain/kotlin/com/example/saltplayerremote/ui/screen/DeviceDiscoveryScreen.kt
package com.example.saltplayerremote.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.saltplayerremote.ui.viewmodel.DeviceDiscoveryViewModel
import org.koin.androidx.compose.getViewModel

object DeviceDiscoveryScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: DeviceDiscoveryViewModel = getViewModel()
        val state by viewModel.state.collectAsState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = state.customIp,
                onValueChange = { viewModel.setCustomIp(it) },
                label = { Text("Custom IP") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = { viewModel.connectCustom() },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Connect")
            }
            Spacer(Modifier.height(16.dp))
            LazyColumn {
                items(state.devices) { device ->
                    TextButton(onClick = { navigator.push(PlayerScreen(device)) }) {
                        Text(device)
                    }
                }
            }
            if (state.isScanning) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            Button(
                onClick = { viewModel.scanLan() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Scan LAN")
            }
        }
    }
}