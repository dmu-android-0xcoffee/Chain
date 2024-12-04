package net.wh64.chain.ui.page

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import net.wh64.chain.ActivityContainer
import net.wh64.chain.R
import net.wh64.chain.logout

@Composable
fun SetInfo(content: @Composable () -> Unit) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween,
		modifier = Modifier.fillMaxWidth().height(65.dp)
	) {
		content.invoke()
	}
}

@Composable
fun Setting(container: ActivityContainer, modifier: Modifier = Modifier) {
	Column(modifier = modifier.fillMaxSize().padding(horizontal = 15.dp)) {
		Row(
			horizontalArrangement = Arrangement.End,
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.fillMaxWidth().height(65.dp)
		) {
			Button(onClick = {
				logout(container.act, container.ctx)
			}) {
				Icon(painterResource(R.drawable.logout_24px), null)
				Text("Logout")
			}
		}
	}
}
