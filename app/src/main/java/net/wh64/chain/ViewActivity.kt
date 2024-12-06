package net.wh64.chain

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.rememberFusedLocationSource
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.wh64.chain.controller.ActionController
import net.wh64.chain.controller.UserController
import net.wh64.chain.data.PageState
import net.wh64.chain.ui.CustomNavbar
import net.wh64.chain.ui.CustomTopBar
import net.wh64.chain.ui.page.Home
import net.wh64.chain.ui.page.Maps
import net.wh64.chain.ui.page.Setting
import net.wh64.chain.ui.theme.ChainTheme
import net.wh64.chain.util.client

class ViewActivity : ComponentActivity() {
	private var backPressedTime: Long = 0
	private val backPressedDuration = 2_000L

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val token = intent.getStringExtra("token")!!
		enableEdgeToEdge()

		runBlocking {
			val res = client.get("${this@ViewActivity.getString(R.string.api_url)}/auth/me") {
				method = HttpMethod.Get
				header("Authorization", "Basic $token")
			}

			if (res.status != HttpStatusCode.OK) {
				Toast.makeText(this@ViewActivity, "토큰이 만료 되었어요! 다시 로그인 해주세요.", Toast.LENGTH_LONG)
					.show()
				finish()
			}
		}

		onBackPressedDispatcher.addCallback {
			if (System.currentTimeMillis() - backPressedTime >= backPressedDuration) {
				backPressedTime = System.currentTimeMillis()
				Toast.makeText(this@ViewActivity, "한 번 더 누르면 종료 됩니다", Toast.LENGTH_SHORT).show()
			} else {
				finishAffinity()
			}
		}

		setContent {
			val user = UserController(token, this)
			val container = ActivityContainer(this, this, user)

			ChainTheme {
				View(container, modifier = Modifier.fillMaxSize())
			}
		}
	}
}

data class ActivityContainer(
	val act: Activity,
	val ctx: Context,
	val user: UserController,
)

data class StateProvider(
	val page: MutableState<PageState>,
	val target: MutableState<String>,
)

@Composable
fun View(
	container: ActivityContainer,
	modifier: Modifier = Modifier
) {
	val scope = rememberCoroutineScope()
	val target = remember { mutableStateOf("") }
	val state = rememberSaveable { mutableStateOf(PageState.HOME) }
	val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(container.ctx) }

	val states = StateProvider(state, target)

	RequestLocationPermission(
		onPermissionGranted = {
			TrackLocation(container, fusedLocationClient)
			Scaffold(
				topBar = { CustomTopBar(controller = container.user) },
				bottomBar = { CustomNavbar(state) },
				modifier = modifier,
			) { innerPadding ->
				when (state.value) {
					PageState.HOME -> Home(container, states, Modifier.padding(innerPadding))
					PageState.MAP -> Maps(container, scope, target.value, Modifier.padding(innerPadding))
					PageState.SETTINGS -> Setting(container, Modifier.padding(innerPadding))
				}
			}
		},
		onPermissionDenied = {
			Toast.makeText(container.ctx, "앱을 이용 하시기 전에 위치 권한을 설정해 주세요!", Toast.LENGTH_LONG).show()
			container.act.finishAffinity()
			return@RequestLocationPermission
		}
	)
}

fun logout(act: Activity, ctx: Context) {
	ActionController(ctx).deleteToken()
	act.finish()
}
