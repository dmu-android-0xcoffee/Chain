package net.wh64.chain.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.runBlocking
import net.wh64.chain.controller.UserController
import net.wh64.chain.ui.theme.Foreground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(controller: UserController) {
	TopAppBar(
		title = {
			Row {
				Box(
					modifier = Modifier.size(60.dp)
						.clip(CircleShape)
						.background(color = Color.Blue)
				)
				Spacer(Modifier.width(10.dp))
				val str = with(StringBuilder()) {
					append(runBlocking {
						controller.getMe()?.name ?: ""
					})
					append("님 환영 합니다!")
				}.lines().joinToString("\n")

				Column(
					verticalArrangement = Arrangement.SpaceBetween,
				) {
					Text(str, fontWeight = FontWeight.Bold)
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
			.height(120.dp)
			.background(
				brush = Brush.verticalGradient(listOf(Color(0xFF101010), Color.Transparent)),
				shape = RectangleShape
			)
	)
}
