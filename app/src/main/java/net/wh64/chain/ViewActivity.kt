package net.wh64.chain

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import net.wh64.chain.ui.CustomTopBar
import net.wh64.chain.ui.theme.ChainTheme
import net.wh64.chain.util.UserController

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
				View(token, modifier = Modifier.fillMaxSize())
			}
		}
	}
}

@Composable
fun View(
	token: String,
	modifier: Modifier = Modifier
) {
	val ctx = LocalContext.current
	val user = UserController(token, ctx)

	Scaffold(
		topBar = { CustomTopBar(controller = user) },
		bottomBar = {},
		modifier = modifier,
	) { innerPadding ->
	}
}