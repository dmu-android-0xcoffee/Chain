package net.wh64.chain

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.wh64.chain.controller.ActionController
import net.wh64.chain.ui.CustomButton
import net.wh64.chain.ui.CustomInputField
import net.wh64.chain.ui.theme.ChainTheme
import net.wh64.chain.controller.LoginForm
import net.wh64.chain.controller.RegisterForm
import net.wh64.chain.controller.UserController

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

		val action = ActionController(this)
		val token = action.getToken()
		if (token != null) {
			with(Intent(this, ViewActivity::class.java)) {
				this.putExtra("token", token)
				startActivity(this)
			}
		}

		setContent {
			ChainTheme {
				Scaffold { innerPadding ->
					LoginView(action, Modifier.padding(innerPadding))
				}
			}
		}
	}
}

@Composable
fun LoginView(action: ActionController, modifier: Modifier = Modifier) {
	val ctx = LocalContext.current
	val scope = rememberCoroutineScope()
	val error = remember {
		listOf(
			mutableStateOf(false),
			mutableStateOf(false),
			mutableStateOf(false)
		)
	}
	val lock = remember { mutableStateOf(false) }
	val register = remember { mutableStateOf(false) }

	val name = remember { mutableStateOf("") }
	val username = remember { mutableStateOf("") }
	val password = rememberSaveable { mutableStateOf("") }

	@Composable
	fun IsRegister(not: @Composable () -> Unit = {}, block: @Composable () -> Unit) {
		if (!register.value) {
			not.invoke()
			return
		}

		block.invoke()
	}

	fun reset() {
		name.value = ""
		username.value = ""
		password.value = ""

		error.forEach {
			it.value = false
		}
	}

	fun enableError() {
		error.forEach {
			it.value = true
		}
	}

	Column(
		modifier = modifier.fillMaxSize(),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Column(
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
		) {
			IsRegister {
				CustomInputField(
					icon = rememberVectorPainter(Icons.Outlined.AccountCircle),
					state = name,
					placeholder = "이름",
					modifier = Modifier.fillMaxWidth(),
					isError = error[0]
				)

				Spacer(Modifier.height(8.dp))
			}

			CustomInputField(
				icon = rememberVectorPainter(Icons.Outlined.Person),
				state = username,
				placeholder = "아이디",
				modifier = Modifier.fillMaxWidth(),
				isError = error[1]
			)

			Spacer(Modifier.height(8.dp))

			CustomInputField(
				secret = true,
				icon = painterResource(R.drawable.password_24px),
				state = password,
				placeholder = "비밀번호",
				modifier = Modifier.fillMaxWidth(),
				isError = error[2]
			)
		}

		Spacer(Modifier.height(30.dp))

		Row(
			horizontalArrangement = Arrangement.Center,
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
		) {
			IsRegister(
				not = {
					LoginBtn(
						register,
						username,
						password,
						lock,
						action,
						enableError = { enableError() },
						reset = { reset() },
						scope,
						ctx
					)
				},
			) {
				RegisterBtn(
					register,
					name,
					username,
					password,
					lock,
					enableError = { enableError() },
					reset = { reset() },
					scope,
					ctx,
				)
			}
		}
	}
}

@Composable
fun RowScope.LoginBtn(
	register: MutableState<Boolean>,
	username: MutableState<String>,
	password: MutableState<String>,
	lock: MutableState<Boolean>,
	action: ActionController,
	enableError: () -> Unit,
	reset: () -> Unit,
	scope: CoroutineScope,
	ctx: Context,
) {
	CustomButton(
		onClick = {
			lock.value = true
			if (username.value.isEmpty() || password.value.isEmpty()) {
				enableError.invoke()
				Toast.makeText(ctx, "아이디 또는 비밀번호를 입력 해주세요!", Toast.LENGTH_LONG).show()
				lock.value = false

				return@CustomButton
			}

			scope.launch {
				val token = UserController.login(ctx, LoginForm(username.value, password.value))
				if (token == null) {
					enableError.invoke()
					Toast.makeText(ctx, "아이디, 비밀번호가 일치 하지 않아요", Toast.LENGTH_LONG).show()
					lock.value = false

					return@launch
				}

				action.injectToken(token)
				with(Intent(ctx, ViewActivity::class.java)) {
					putExtra("token", token)

					Toast.makeText(ctx, "Login Success!", Toast.LENGTH_LONG).show()
					lock.value = false
					ctx.startActivity(this)
				}
			}
		},
		disabled = lock.value,
		modifier = Modifier.weight(1f, true),
	) {
		Icon(painterResource(R.drawable.login_24px), null)
		Text(text = "로그인")
	}

	Spacer(Modifier.width(10.dp))

	CustomButton(
		onClick = {
			register.value = true
			reset.invoke()
		},
		disabled = lock.value,
		modifier = Modifier.weight(1f, true),
	) {
		Icon(Icons.Outlined.Add, null)
		Text(text = "회원가입")
	}
}

@Composable
fun RowScope.RegisterBtn(
	register: MutableState<Boolean>,
	name: MutableState<String>,
	username: MutableState<String>,
	password: MutableState<String>,
	lock: MutableState<Boolean>,
	enableError: () -> Unit,
	reset: () -> Unit,
	scope: CoroutineScope,
	ctx: Context,
) {
	CustomButton(
		onClick = {
			scope.launch {
				if (name.value == "" || username.value == "" || password.value == "") {
					Toast.makeText(ctx, "이름, 아이디, 비밀번호를 입력 해주세요!", Toast.LENGTH_LONG).show()
					enableError.invoke()
					lock.value = false

					return@launch
				}

				try {
					UserController.register(ctx, RegisterForm(name.value, username.value, password.value))
				} catch (ex: Exception) {
					Toast.makeText(ctx, "이미 사용중인 아이디예요. 다른 아이디를 사용 해주세요.", Toast.LENGTH_LONG).show()
					ex.printStackTrace()
					lock.value = false

					return@launch
				}

				Toast.makeText(ctx, "회원가입을 마쳤어요. 다시 로그인 해주세요!", Toast.LENGTH_LONG).show()
				reset.invoke()

				lock.value = false
				register.value = false
			}
		},
		disabled = lock.value,
		modifier = Modifier.weight(1f, true)
	) {
		Icon(Icons.Outlined.Create, null)
		Text("회원가입")
	}
	Spacer(Modifier.width(10.dp))

	CustomButton(
		onClick = {
			register.value = !register.value
			reset.invoke()
		},
		disabled = lock.value,
		modifier = Modifier.weight(1f, true)
	) {
		Icon(Icons.AutoMirrored.Outlined.ArrowBack, null)
		Text("돌아가기")
	}
}
