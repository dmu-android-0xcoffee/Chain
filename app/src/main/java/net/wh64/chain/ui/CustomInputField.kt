package net.wh64.chain.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import net.wh64.chain.R

@Composable
fun CustomInputField(
	placeholder: String,
	secret: Boolean = false,
	icon: Painter? = null,
	isError: MutableState<Boolean>,
	state: MutableState<String>,
	modifier: Modifier = Modifier
) {
	val show = remember { mutableStateOf(false) }
	val btn = if (show.value) {
		painterResource(R.drawable.visibility_off_24px)
	} else {
		painterResource(R.drawable.visibility_24px)
	}

	OutlinedTextField(
		singleLine = true,
		value = state.value,
		label = { Text(text = placeholder) },
		isError = isError.value,
		leadingIcon = {
			if (icon != null)
				Icon(icon, contentDescription = null)
		},
		visualTransformation = if (secret && !show.value) {
			PasswordVisualTransformation()
		} else {
			VisualTransformation.None
		},
		trailingIcon = {
			if (secret) {
				IconButton(onClick = { show.value = !show.value }, enabled = state.value.isNotEmpty()) {
					Icon(btn, contentDescription = null)
				}
			}
		},
		onValueChange = {
			state.value = it
			isError.value = false
		},
		modifier = modifier
	)
}
