package net.wh64.chain.ui.page

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.wh64.chain.ActivityContainer
import net.wh64.chain.R
import net.wh64.chain.StateProvider
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
	val users = remember { mutableStateOf<List<FriendRef>>(emptyList()) }
	val scope = rememberCoroutineScope()

	scope.launch {
		users.value = container.user.getFriends().friends
	}

	Spacer(Modifier.height(15.dp))
	LazyColumn {
		itemsIndexed(users.value) { _, user ->
			Friend(container, states, user.data, user.accept)
		}
	}
}

@Composable
fun Friend(container: ActivityContainer, states: StateProvider, user: ChainUser, accept: Boolean) {
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
					.background(color = Color.Blue)
			)
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
				onClick = {},
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
