package com.bluechat.data.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.bluechat.domain.model.Message
import com.bluechat.domain.repository.MessageRepository
import com.bluechat.util.Constants
import kotlinx.coroutines.*
import java.io.IOException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@SuppressLint("MissingPermission")
@Singleton
class BluetoothManager @Inject constructor(
    private val context: Context,
    private val messageRepository: MessageRepository
) {
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var bluetoothGatt: BluetoothGatt? = null
    private var bluetoothServerSocket: BluetoothServerSocket? = null
    private var currentConnection: BluetoothSocket? = null
    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var messageCallback: ((Message) -> Unit)? = null

    val isBluetoothEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    val isBluetoothAvailable: Boolean
        get() = bluetoothAdapter != null

    val pairedDevices: List<BluetoothDevice>
        get() = if (hasBluetoothPermission()) {
            bluetoothAdapter?.bondedDevices?.toList() ?: emptyList()
        } else {
            emptyList()
        }

    fun startServer(callback: (Message) -> Unit) {
        messageCallback = callback
        coroutineScope.launch {
            try {
                val serverSocket = bluetoothAdapter?.listenUsingRfcommWithServiceRecord(
                    Constants.APP_NAME,
                    UUID.fromString(Constants.SERVICE_UUID)
                ) ?: return@launch

                bluetoothServerSocket = serverSocket

                while (true) {
                    val socket = serverSocket.accept()
                    currentConnection = socket
                    handleConnection(socket)
                }
            } catch (e: IOException) {
                // Handle error
            }
        }
    }

    fun connectToDevice(device: BluetoothDevice, callback: (Boolean) -> Unit) {
        coroutineScope.launch {
            try {
                val socket = device.createRfcommSocketToServiceRecord(
                    UUID.fromString(Constants.SERVICE_UUID)
                )
                socket.connect()
                currentConnection = socket
                callback(true)
            } catch (e: IOException) {
                callback(false)
            }
        }
    }

    fun sendMessage(message: Message) {
        coroutineScope.launch {
            try {
                val json = message.toJson()
                currentConnection?.outputStream?.write(json.toByteArray())
                messageRepository.saveMessage(message)
            } catch (e: IOException) {
                // Handle error
            }
        }
    }

    private fun handleConnection(socket: BluetoothSocket) {
        coroutineScope.launch {
            try {
                val buffer = ByteArray(1024)
                val inputStream = socket.inputStream
                while (true) {
                    val bytes = inputStream.read(buffer)
                    if (bytes > 0) {
                        val messageJson = String(buffer, 0, bytes)
                        val message = Message.fromJson(messageJson)
                        messageCallback?.invoke(message)
                        messageRepository.saveMessage(message)
                    }
                }
            } catch (e: IOException) {
                // Handle disconnection
                currentConnection = null
            }
        }
    }

    fun stop() {
        coroutineScope.cancel()
        currentConnection?.close()
        bluetoothServerSocket?.close()
        bluetoothGatt?.close()
    }

    private fun hasBluetoothPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    companion object {
        private const val TAG = "BluetoothManager"
    }
}
