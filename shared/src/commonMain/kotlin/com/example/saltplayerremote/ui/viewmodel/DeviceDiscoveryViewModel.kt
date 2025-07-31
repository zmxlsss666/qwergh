// shared/src/commonMain/kotlin/com/example/saltplayerremote/ui/viewmodel/DeviceDiscoveryViewModel.kt
package com.example.saltplayerremote.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.net.InetAddress

data class DeviceDiscoveryState(
    val devices: List<String> = emptyList(),
    val isScanning: Boolean = false,
    val customIp: String = ""
)

class DeviceDiscoveryViewModel : ViewModel() {
    private val _state = MutableStateFlow(DeviceDiscoveryState())
    val state = _state.asStateFlow()

    fun setCustomIp(ip: String) {
        _state.value = _state.value.copy(customIp = ip)
    }

    fun scanLan() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isScanning = true, devices = emptyList())
            val subnet = "192.168.0"
            val port = 35373
            val jobs = (1..255).map { i ->
                async(Dispatchers.IO) {
                    val ip = "$subnet.$i"
                    try {
                        val address = InetAddress.getByName(ip)
                        if (address.isReachable(100)) {
                            val socket = java.net.Socket()
                            socket.connect(java.net.InetSocketAddress(ip, port), 300)
                            socket.close()
                            ip
                        } else null
                    } catch (e: Exception) {
                        null
                    }
                }
            }
            val results = jobs.awaitAll().filterNotNull()
            _state.value = _state.value.copy(isScanning = false, devices = results)
        }
    }

    fun connectCustom() {
        viewModelScope.launch {
            val ip = _state.value.customIp
            try {
                val address = InetAddress.getByName(ip)
                if (address.isReachable(1000)) {
                    // TODO: notify navigation
                }
            } catch (e: Exception) {
                // ignore
            }
        }
    }
}