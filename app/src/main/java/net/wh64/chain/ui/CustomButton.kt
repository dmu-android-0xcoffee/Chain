package net.wh64.chain.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CustomButton(
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	disabled: Boolean = false,
	content: @Composable RowScope.() -> Unit
) {
	Button(
		onClick = onClick,
		modifier = modifier,
		enabled = !disabled,
		content = content
	)
}
