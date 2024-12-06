package net.wh64.chain

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import net.wh64.chain.ui.theme.Background
import net.wh64.chain.ui.theme.ChainTheme
import net.wh64.chain.ui.theme.Palette2
import net.wh64.chain.ui.theme.Palette3
import net.wh64.chain.util.client

@Serializable
data class ChatRef(
	val chats: List<ChainMessageData>
)

@Serializable
data class ChainMessageData(
	val id: Int,
	val uid: String,
	val cid: Int,
	val content: String
)

@Serializable
data class ChainMessage(val message: String)

class ChatAction(private val ctx: Context, private val token: String) {
	suspend fun getChats(channel: Int): ChatRef {
		val res = client.get("${ctx.getString(R.string.api_url)}/api/channel/${channel}/chats") {
			header("Authorization", "Basic $token")
		}

		if (res.status != HttpStatusCode.OK) {
			return ChatRef(listOf())
		}

		return res.body()
	}

	suspend fun send(content: String, cid: Int): Boolean {
		val res = client.post("${ctx.getString(R.string.api_url)}/api/chat/${cid}") {
			header("Authorization", "Basic $token")
			header("Content-Type", "application/json")

			setBody(ChainMessage(content))
		}

		return res.status == HttpStatusCode.Created
	}
}

@OptIn(ExperimentalMaterial3Api::class)
class ChatActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		actionBar?.hide()

		val token = intent.getStringExtra("token")!!
		val target = intent.getStringExtra("target")!!
		val titleName = intent.getStringExtra("title_name")!!
		val channel = intent.getIntExtra("channel", -1)

		val action = ChatAction(this, token)

		if (channel == -1) {
			finish()
			return
		}

		setContent {
			val chat = remember { mutableStateOf("") }
			val scope = rememberCoroutineScope()
			val chats = remember { mutableStateOf<List<ChainMessageData>>(emptyList()) }

			scope.launch {
				while (true) {
					chats.value = action.getChats(channel).chats
					delay(500)
				}
			}

			ChainTheme {
				Scaffold(
					topBar = {
						TopAppBar(
							title = {
								Row(
									verticalAlignment = Alignment.CenterVertically,
									horizontalArrangement = Arrangement.Start,
									modifier = Modifier.fillMaxWidth()
								) {
									IconButton(onClick = { finish() }) {
										Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
									}
									Spacer(Modifier.width(10.dp))

									Box(
										modifier = Modifier.size(50.dp)
											.clip(CircleShape)
									) {
										Image(
											painterResource(R.drawable.default_user),
											null,
											Modifier.size(50.dp).clip(CircleShape).background(Color.White)
										)
									}
									Spacer(Modifier.width(10.dp))
									Text(titleName)
								}
							},
							modifier = Modifier
								.height(135.dp)
								.background(
									brush = Brush.verticalGradient(listOf(Color(0xFF101010), Color.Transparent)),
									shape = RectangleShape
								)
						)
					},
					bottomBar = {
						Column(
							verticalArrangement = Arrangement.Top,
							modifier = Modifier.fillMaxWidth().padding(horizontal = 25.dp).height(80.dp)
						) {
							OutlinedTextField(
								value = chat.value,
								singleLine = false,
								leadingIcon = {
									Icon(painterResource(R.drawable.chat_24px), contentDescription = null)
								},
								trailingIcon = {
									IconButton(onClick = {
										scope.launch {
											val res = action.send(chat.value, channel)
											if (!res) {
												Toast.makeText(
													this@ChatActivity,
													"메시지 전송에 실패 했어요.",
													Toast.LENGTH_LONG
												).show()
												return@launch
											}

											chat.value = ""
										}
									}, enabled = chat.value.isNotEmpty()) {
										Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
									}
								},
								onValueChange = { chat.value = it },
								shape = RoundedCornerShape(100.dp),
								modifier = Modifier.fillMaxWidth()
							)

							Spacer(Modifier.height(25.dp))
						}
					},
					modifier = Modifier.fillMaxSize()
				) { paddingValues ->
					Column(
						verticalArrangement = Arrangement.Bottom,
						modifier = Modifier.background(Background).fillMaxSize()
							.padding(paddingValues)
							.padding(horizontal = 25.dp),
					) {
						Column(
							verticalArrangement = Arrangement.Bottom,
						) {
							chats.value.forEach {
								Spacer(Modifier.height(10.dp))

								Column {
									Text(if (it.uid == target) titleName else "Me", fontSize = 20.sp, fontWeight = FontWeight.Bold)

									Column(modifier = Modifier) {
										Text(it.content)
									}

									Spacer(Modifier.height(10.dp))
								}
							}
						}
					}
				}
			}
		}
	}
}