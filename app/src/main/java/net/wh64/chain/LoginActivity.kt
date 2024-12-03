package net.wh64.chain

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import net.wh64.chain.ui.CustomButton
import net.wh64.chain.ui.CustomInputField
import net.wh64.chain.ui.CustomSecretInputBtn
import net.wh64.chain.ui.theme.ChainTheme
import net.wh64.chain.util.client

class LoginActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		installSplashScreen().apply {
			setOnExitAnimationListener { provider ->
				ObjectAnimator.ofFloat(
					provider.view,
					"translationY",
					0.5f, 0f
				).apply {
					interpolator = OvershootInterpolator()
					duration = 300
					doOnEnd {
						provider.remove()
					}

					start()
				}
			}
		}
		actionBar?.hide()
		enableEdgeToEdge()

		val sp = getSharedPreferences("login", MODE_PRIVATE)
		val token = sp.getString("token", null)
		if (token != null) {
			with(Intent(this, ViewActivity::class.java)) {
				this.putExtra("token", token)
				startActivity(this)
			}
		}

		setContent {
			ChainTheme {
				Scaffold { innerPadding ->
					LoginView(Modifier.padding(innerPadding))
				}
			}
		}
	}
}

@Serializable
data class LoginForm(
	val username: String,
	val password: String
)

@Serializable
data class LoginResponse(val token: String)

@Composable
fun LoginView(modifier: Modifier = Modifier) {
	val ctx = LocalContext.current
	val scope = rememberCoroutineScope()
	val username = rememberSaveable { mutableStateOf("") }
	val password = rememberSaveable { mutableStateOf("") }
	val show = remember { mutableStateOf(false) }

	Column(
		modifier = modifier.fillMaxSize(),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Column(
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
		) {
			CustomInputField(
				icon = rememberVectorPainter(Icons.Outlined.Person),
				state = username,
				placeholder = "Username",
				modifier = Modifier.fillMaxWidth(),
			)

			Spacer(Modifier.height(8.dp))

			CustomInputField(
				secret = true,
				icon = painterResource(R.drawable.password_24px),
				state = password,
				placeholder = "Password",
				modifier = Modifier.fillMaxWidth(),
				btnData = CustomSecretInputBtn(
					show = show.value,
					icon = if (show.value) {
						painterResource(R.drawable.visibility_off_24px)
					} else {
						painterResource(R.drawable.visibility_24px)
					},
					onClick = {
						show.value = !show.value
					}
				)
			)
		}

		Spacer(Modifier.height(30.dp))

		Row(
			horizontalArrangement = Arrangement.Center,
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
		) {
			CustomButton(
				onClick = {
					if (username.value.isEmpty() || password.value.isEmpty()) {
						Toast.makeText(ctx, "아이디 또는 비밀번호를 입력 해주세요!", Toast.LENGTH_LONG).show()
						return@CustomButton
					}

					scope.launch {
						val res = client.post(
							"${ctx.getString(R.string.api_url)}:8080/auth/login"
						) {
							headers["Content-Type"] = "application/json"
							setBody(LoginForm(username.value, password.value))
						}

						if (res.status != HttpStatusCode.OK) {
							Toast.makeText(ctx, "아이디 또는 비밀번호가 일치 하지 않아요", Toast.LENGTH_LONG).show()
							return@launch
						}

						Toast.makeText(ctx, "Login Success!", Toast.LENGTH_LONG).show()
						val body = res.body<LoginResponse>()
						val sp = ctx.getSharedPreferences("login", Activity.MODE_PRIVATE)
						with(sp.edit()) {
							putString("token", body.token)
							commit()
						}
					}
				},
				modifier = Modifier.weight(1f, true),
			) {
				Icon(painterResource(R.drawable.login_24px), contentDescription = "")
				Text(text = "로그인")
			}

			Spacer(Modifier.width(10.dp))

			CustomButton(
				onClick = {},
				modifier = Modifier.weight(1f, true),
			) {
				Icon(Icons.Outlined.Add, contentDescription = "")
				Text(text = "회원가입")
			}
		}
	}
}
