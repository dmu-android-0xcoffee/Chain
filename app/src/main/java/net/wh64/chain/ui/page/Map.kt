package net.wh64.chain.ui.page

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.compose.*
import net.wh64.chain.ActivityContainer
import net.wh64.chain.R

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun Maps(container: ActivityContainer, modifier: Modifier = Modifier) {
	val scope = rememberCoroutineScope()
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
		onLocationChange = { loc ->
			Log.d("location", loc.toString())
		},
		modifier = modifier
	)

//	Column(modifier = modifier.fillMaxSize().padding(horizontal = 15.dp)) {
//		// TODO: map view screen
//	}
}
