package net.wh64.chain

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.rememberFusedLocationSource
import kotlinx.coroutines.launch
import net.wh64.chain.controller.ActionController
import net.wh64.chain.controller.UserController
import net.wh64.chain.data.PageState
import net.wh64.chain.ui.CustomNavbar
import net.wh64.chain.ui.CustomTopBar
import net.wh64.chain.ui.page.Chat
import net.wh64.chain.ui.page.Home
import net.wh64.chain.ui.page.Maps
import net.wh64.chain.ui.page.Setting
import net.wh64.chain.ui.theme.ChainTheme

class ViewActivity : ComponentActivity() {
	private var backPressedTime: Long = 0
	private val backPressedDuration = 2_000L

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val token = intent.getStringExtra("token")!!
		enableEdgeToEdge()

		onBackPressedDispatcher.addCallback {
			if (System.currentTimeMillis() - backPressedTime >= backPressedDuration) {
				backPressedTime = System.currentTimeMillis()
				Toast.makeText(this@ViewActivity, "한 번 더 누르면 종료 됩니다", Toast.LENGTH_SHORT).show()
			} else {
				finishAffinity()
			}
		}

		setContent {
			ChainTheme {
				View(this, token, modifier = Modifier.fillMaxSize())
			}
		}
	}
}

data class ActivityContainer(
	val act: Activity,
	val ctx: Context,
	val user: UserController,
)

@Composable
fun View(
	act: Activity,
	token: String,
	modifier: Modifier = Modifier
) {
	val ctx = LocalContext.current
	val scope = rememberCoroutineScope()
	val user = UserController(token, ctx)
	val container = ActivityContainer(act, ctx, user)
	val state = rememberSaveable { mutableStateOf(PageState.HOME) }

	Scaffold(
		topBar = { CustomTopBar(controller = user) },
		bottomBar = { CustomNavbar(state) },
		modifier = modifier,
	) { innerPadding ->
		when (state.value) {
			PageState.HOME -> Home(container, Modifier.padding(innerPadding))
			PageState.MAP -> Maps(container, scope, Modifier.padding(innerPadding))
			PageState.CHAT -> Chat(container, Modifier.padding(innerPadding))
			PageState.SETTINGS -> Setting(container, Modifier.padding(innerPadding))
		}
	}
}

fun logout(act: Activity, ctx: Context) {
	ActionController(ctx).deleteToken()
	act.finish()
}
