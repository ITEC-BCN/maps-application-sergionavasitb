package com.example.mapsapp

import android.R.attr.data
import android.app.Application
import android.content.Intent
import android.media.audiofx.BassBoost.Settings
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import com.example.mapsapp.utils.PermissionStatus


@Composable
fun MyApp(activity: ComponentActivity) {
    val myViewModel = viewModel<PermissionViewModel>()
    val permissionStatus = myViewModel.permissionStatus.value
    var alreadyRequested by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        val result = when {
            granted -> PermissionStatus.Granted
            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> PermissionStatus.Denied

            else -> PermissionStatus.PermanentlyDenied
        }
        myViewModel.updatePermissionStatus(result)
    }

    LaunchedEffect(Unit) {
        if (!alreadyRequested) {
            alreadyRequested = true
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (permissionStatus) {
            null -> {
                CircularProgressIndicator()
                Text("Requesting permission...")
            }
            PermissionStatus.Granted -> Text("Permission granted")
            PermissionStatus.Denied -> {
                Text("Permission denied")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }) {
                    Text("Apply again")
                }
            }

            PermissionStatus.PermanentlyDenied -> {
                Text("Permission permanently denied")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", activity.packageName, null)
                    }
                    activity.startActivity(intent)
                }) {
                    Text("Go to settings")
                }
            }
        }
    }
}