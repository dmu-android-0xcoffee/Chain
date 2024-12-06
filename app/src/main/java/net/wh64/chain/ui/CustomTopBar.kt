package net.wh64.chain.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.*
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.wh64.chain.R
import net.wh64.chain.controller.UserController
import net.wh64.chain.data.UserStatus
import net.wh64.chain.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(controller: UserController) {
	val scope = rememberCoroutineScope()
	val data = remember { mutableStateOf(runBlocking { controller.getMe() }) }
	LaunchedEffect(data.value) {
		scope.launch {
			while (true) {
				data.value = controller.getMe()
				delay(500)
			}
		}
	}

	if (data.value == null) {
		return
	}

	TopAppBar(
		title = {
			Row {
				Box(
					modifier = Modifier.size(60.dp)
						.clip(CircleShape)
				) {
					Image(
						painterResource(R.drawable.default_user),
						null,
						Modifier.size(60.dp).clip(CircleShape).background(Color.White)
					)
				}
				Spacer(Modifier.width(10.dp))
				val str = with(StringBuilder()) {
					append(data.value?.name)
					append("님 환영 합니다!")
				}.lines().joinToString("\n")

				Column(
					verticalArrangement = Arrangement.SpaceBetween,
				) {
					Row(verticalAlignment = Alignment.CenterVertically) {
						Box(modifier = Modifier.size(10.dp).clip(CircleShape)
							.background(
								color = when (data.value?.status) {
									UserStatus.ONLINE -> Online
									UserStatus.IDLE -> Idle
									UserStatus.OFFLINE -> Offline
									UserStatus.DO_NOT_DISTURB -> DoNotDisturb
									else -> Offline
								}
							)
						)
						Spacer(Modifier.width(5.dp))
						Text(str, fontWeight = FontWeight.Bold)
					}
					Text("현재 온라인인 친구: 0명", fontSize = 16.sp)
				}
			}
		},
		colors = TopAppBarColors(
			containerColor = Color.Transparent,
			scrolledContainerColor = Color.Transparent,
			navigationIconContentColor = Foreground,
			actionIconContentColor = Foreground,
			titleContentColor = Foreground,
		),
		modifier = Modifier
			.height(135.dp)
			.background(
				brush = Brush.verticalGradient(listOf(Color(0xFF101010), Color.Transparent)),
				shape = RectangleShape
			)
	)
}
