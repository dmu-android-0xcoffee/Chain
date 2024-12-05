package net.wh64.chain.ui.page

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.wh64.chain.ActivityContainer
import net.wh64.chain.controller.ChainChannelData

@Composable
fun Channel(data: ChainChannelData) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween,
		modifier = Modifier.fillMaxWidth().height(65.dp)
	) {
		data.name
	}
}


@Composable
fun Chat(container: ActivityContainer, modifier: Modifier = Modifier) {
	Column(modifier = modifier.fillMaxSize().padding(horizontal = 15.dp)) {
		// TODO: make chat screen
	}
}
