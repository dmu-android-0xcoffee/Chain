package net.wh64.chain.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

data class CustomSecretInputBtn(
	val show: Boolean,
	val icon: Painter,
	val onClick: () -> Unit,
)

@Composable
fun CustomInputField(
	placeholder: String,
	secret: Boolean = false,
	icon: Painter? = null,
	btnData: CustomSecretInputBtn? = null,
	state: MutableState<String>,
	modifier: Modifier = Modifier
) {
//	val show = remember { mutableStateOf(false) }

	OutlinedTextField(
		singleLine = true,
		value = state.value,
		label = { Text(text = placeholder) },
		leadingIcon = {
			if (icon != null)
				Icon(icon, contentDescription = null)
		},
		visualTransformation = if (secret && btnData?.show == false) {
			PasswordVisualTransformation()
		} else {
			VisualTransformation.None
		},
		trailingIcon = {
			if (secret && btnData != null) {
				IconButton(onClick = btnData.onClick, enabled = state.value.isNotEmpty()) {
					Icon(btnData.icon, contentDescription = null)
				}
			}
		},
		onValueChange = { state.value = it },
		modifier = modifier
	)
}
