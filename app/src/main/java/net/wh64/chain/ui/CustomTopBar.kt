package net.wh64.chain.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.runBlocking
import net.wh64.chain.controller.UserController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(controller: UserController) {
	val scope = rememberCoroutineScope()

	TopAppBar(title = {
		Row {
			Box(
				modifier = Modifier.size(60.dp)
					.clip(CircleShape)
					.background(color = Color.Blue)
			) {}
			Spacer(Modifier.width(10.dp))
			Text("${runBlocking {
				controller.getMe()?.name ?: ""
			}}님 환영합니다!", fontWeight = FontWeight.Bold)
		}
	})
}
