package net.wh64.chain

import android.Manifest
import android.annotation.SuppressLint
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.launch
import net.wh64.chain.controller.LocationData
import net.wh64.chain.data.UserStatus

@Composable
fun RequestLocationPermission(
	onPermissionGranted: @Composable () -> Unit,
	onPermissionDenied: @Composable () -> Unit
) {
	val permissionState = remember { mutableStateOf<Boolean?>(null) }

	val permissionLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.RequestPermission()
	) { isGranted ->
		permissionState.value = isGranted
	}

	LaunchedEffect(Unit) {
		permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
	}

	when (permissionState.value) {
		true -> onPermissionGranted()
		false -> onPermissionDenied()
		null -> {}
	}
}

@Composable
@SuppressLint("MissingPermission")
fun TrackLocation(container: ActivityContainer, fusedLocationClient: FusedLocationProviderClient) {
	val scope = rememberCoroutineScope()

	LaunchedEffect(Unit) {
		val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 500L).build()

		val locationCallback = object : LocationCallback() {
			override fun onLocationResult(locationResult: LocationResult) {
				locationResult.locations.lastOrNull()?.let { location ->
					scope.launch {
						val me = container.user.getMe() ?: return@launch
						if (me.status == UserStatus.ONLINE || me.status == UserStatus.IDLE)
							container.user.saveLocation(LocationData(location.longitude, location.latitude))
					}
				}
			}
		}

		fusedLocationClient.requestLocationUpdates(
			locationRequest,
			locationCallback,
			Looper.getMainLooper()
		)

		DisposableEffectScope().onDispose {
			fusedLocationClient.removeLocationUpdates(locationCallback)
		}
	}
}
