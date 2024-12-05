package net.wh64.chain.ui.page

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.compose.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.wh64.chain.ActivityContainer
import net.wh64.chain.R
import net.wh64.chain.controller.LocationData
import net.wh64.chain.controller.LocationDataResp
import net.wh64.chain.data.FriendRef
import net.wh64.chain.ui.theme.Online

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun Maps(container: ActivityContainer, scope: CoroutineScope, moveTo: String, modifier: Modifier = Modifier) {
	val users = remember { mutableStateOf<List<LocationDataResp>>(emptyList()) }
	scope.launch {
		users.value = container.user.getFriendLocations()
	}

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
		onMapLoaded = {
			if (moveTo.isNotEmpty()) {
				val user = users.value.singleOrNull { it.target == moveTo }
				if (user != null) {
					Log.d("POS", moveTo)
					moveCameraWithPosition(LatLng(user.lat, user.lng), cameraPositionState)
				}
			}
		},
		uiSettings = MapUiSettings(
			isLocationButtonEnabled = true,
		),
		modifier = modifier
	) {
		for (it in users.value) {
			Marker(
				state = MarkerState(position = LatLng(it.lat, it.lng)),
				captionText = it.target,
			)
		}
	}
}

@OptIn(ExperimentalNaverMapApi::class)
fun moveCameraWithPosition(position: LatLng, cameraPositionState: CameraPositionState) {
	val update = CameraUpdate.scrollTo(position)
	cameraPositionState.move(update)
}
