package com.example.mapsapp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mapsapp.utils.PermissionStatus
import androidx.compose.runtime.State

class PermissionViewModel: ViewModel() {
    private val _permissionStatus = mutableStateOf<PermissionStatus?>(null)
    val permissionStatus: State<PermissionStatus?> = _permissionStatus

    fun updatePermissionStatus(status: PermissionStatus) {
        _permissionStatus.value = status
    }
}