package net.wh64.chain.ui.page

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.wh64.chain.ActivityContainer
import net.wh64.chain.ChatActivity
import net.wh64.chain.R
import net.wh64.chain.StateProvider
import net.wh64.chain.controller.ChainChannelData
import net.wh64.chain.data.ChainUser
import net.wh64.chain.data.FriendRef
import net.wh64.chain.data.PageState
import net.wh64.chain.data.UserStatus
import net.wh64.chain.ui.theme.DoNotDisturb
import net.wh64.chain.ui.theme.Idle
import net.wh64.chain.ui.theme.Offline
import net.wh64.chain.ui.theme.Online

@Composable
fun Home(container: ActivityContainer, states: StateProvider, modifier: Modifier = Modifier) {
	val search = remember { mutableStateOf("") }

	Column(modifier = modifier.fillMaxSize().padding(horizontal = 15.dp)) {
		Spacer(Modifier.height(15.dp))
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween,
		) {
			OutlinedTextField(
				value = search.value,
				shape = RoundedCornerShape(100.dp),
				onValueChange = { search.value = it },
				singleLine = true,
				leadingIcon = {
					Icon(Icons.Outlined.Search, contentDescription = null)
				},
				placeholder = { Text("Search User") },
				modifier = Modifier.weight(1f),
			)

			Spacer(Modifier.width(10.dp))

			IconButton(
				onClick = {
					Toast.makeText(container.ctx, "${search.value} 님에게 친추를 보냈어요!", Toast.LENGTH_LONG).show()
					search.value = ""
				},
				Modifier.scale(1.2f)
			) {
				Icon(
					painterResource(R.drawable.person_add_24px),
					contentDescription = null,
					modifier = Modifier.fillMaxSize(0.6f)
				)
			}
		}

		FriendList(container, states)
	}
}

@Composable
fun FriendList(container: ActivityContainer, states: StateProvider) {
	val me = remember { mutableStateOf<ChainUser?>(null) }
	val users = remember { mutableStateOf<List<FriendRef>>(emptyList()) }
	val scope = rememberCoroutineScope()

	scope.launch {
		while (true) {
			if (me.value == null) {
				me.value = container.user.getMe()
			}

			users.value = container.user.getFriends().friends
			delay(500)
		}
	}

	Spacer(Modifier.height(15.dp))
	LazyColumn {
		itemsIndexed(users.value) { _, user ->
			Friend(container, states, me.value!!, user.data, user.accept)
		}
	}
}

@Composable
fun Friend(container: ActivityContainer, states: StateProvider, me: ChainUser, user: ChainUser, accept: Boolean) {
	val scope = rememberCoroutineScope()

	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween,
		modifier = Modifier.fillMaxWidth().height(60.dp)
			.clickable {
				states.target.value = user.username
				states.page.value	= PageState.MAP
			}
	) {
		if (!accept) {
			return
		}

		Row(verticalAlignment = Alignment.CenterVertically) {
			Spacer(Modifier.width(15.dp))
			Box(
				modifier = Modifier.size(50.dp)
					.clip(CircleShape)
					.background(color = Color.White)
			) {
				Image(
					painterResource(R.drawable.default_user),
					null,
					Modifier.size(50.dp)
				)
			}
			Spacer(Modifier.width(10.dp))

			Row(
				verticalAlignment = Alignment.CenterVertically,
			) {
				Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(
					color = when (runBlocking { user.status }) {
						UserStatus.ONLINE -> Online
						UserStatus.IDLE -> Idle
						UserStatus.OFFLINE -> Offline
						UserStatus.DO_NOT_DISTURB -> DoNotDisturb
					})
				)
				Spacer(Modifier.width(5.dp))
				Text(user.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
			}
		}

		Row {
			IconButton(
				onClick = {
					scope.launch {
						var chan = container.user.getChannel(user.username)
						if (chan == null) {
							val done = container.user.createChannel(user.username)
							if (!done) {
								Toast.makeText(container.ctx, "unknown error", Toast.LENGTH_LONG).show()
								return@launch
							}

							chan = container.user.getChannel(user.username)
							Toast.makeText(container.ctx, "entering channel: ${chan?.id}", Toast.LENGTH_LONG)
								.show()
						}

						with(Intent(container.ctx, ChatActivity::class.java)) {
							putExtra("token", container.user.token)
							putExtra("title_name", user.name)
							putExtra("target", user.username)
							putExtra("channel", chan?.id)

							container.ctx.startActivity(this)
						}
					}
				},
				modifier = Modifier.scale(1.2f)
			) {
				Icon(
					painterResource(R.drawable.chat_24px),
					contentDescription = null,
					modifier = Modifier.fillMaxSize(0.6f)
				)
			}
			Spacer(Modifier.width(15.dp))
		}
	}
}
