package net.wh64.chain.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import net.wh64.chain.R
import net.wh64.chain.data.PageState
import net.wh64.chain.ui.theme.Background2
import net.wh64.chain.ui.theme.Foreground
import net.wh64.chain.ui.theme.Palette1

@Composable
fun CustomNavbar(state: MutableState<PageState>) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier.fillMaxWidth().height(90.dp).background(Background2)
	) {
		Row(
			verticalAlignment = Alignment.Top,
			horizontalArrangement = Arrangement.SpaceBetween,
			modifier = Modifier.fillMaxWidth().padding(horizontal = 55.dp, vertical = 10.dp)
		) {
			@Composable
			fun NavItem(target: PageState, icon: ImageVector) {
				IconButton(onClick = { state.value = target }) {
					Icon(
						icon,
						contentDescription = null,
						tint = if (state.value == target) Palette1 else Foreground,
						modifier = Modifier.size(30.dp)
					)
				}
			}

			NavItem(target = PageState.HOME, icon = Icons.Default.Home)
			NavItem(target = PageState.MAP, icon = Icons.Default.LocationOn)
			NavItem(target = PageState.SETTINGS, icon = Icons.Default.Settings)
		}

		Spacer(Modifier.height(50.dp))
	}
}
