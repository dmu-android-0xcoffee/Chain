package net.wh64.chain.ui.page

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.compose.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.wh64.chain.ActivityContainer
import net.wh64.chain.R
import net.wh64.chain.controller.LocationData

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun Maps(container: ActivityContainer, scope: CoroutineScope, modifier: Modifier = Modifier) {
	val cameraPositionState = rememberCameraPositionState {
		position = CameraPosition.INVALID
	}

	NaverMapSdk.getInstance(container.ctx).client = NaverMapSdk
		.NaverCloudPlatformClient(stringResource(R.string.naver_client_id))

	NaverMap(
		locationSource = rememberFusedLocationSource(isCompassEnabled = true),
		properties = MapProperties(
			locationTrackingMode = LocationTrackingMode.Follow,
		),
		cameraPositionState = cameraPositionState,
		uiSettings = MapUiSettings(
			isLocationButtonEnabled = true,
		),
		onLocationChange = { location ->
			scope.launch {
				container.user.saveLocation(LocationData(location.longitude, location.latitude))
			}
		},
		modifier = modifier
	)

	scope.launch {
	}

//	Column(modifier = modifier.fillMaxSize().padding(horizontal = 15.dp)) {
//		// TODO: map view screen
//	}
}
