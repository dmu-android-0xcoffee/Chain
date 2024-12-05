package net.wh64.chain.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import net.wh64.chain.ActivityContainer
import net.wh64.chain.R

@Composable
fun Home(container: ActivityContainer, modifier: Modifier = Modifier) {
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
				onClick = {},
				Modifier.scale(1.2f)
			) {
				Icon(
					painterResource(R.drawable.person_add_24px),
					contentDescription = null,
					modifier = Modifier.fillMaxSize(0.6f)
				)
			}
		}
	}
}
