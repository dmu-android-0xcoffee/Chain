package net.wh64.chain.ui.page

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.wh64.chain.ActivityContainer
import net.wh64.chain.R
import net.wh64.chain.controller.UpdateForm
import net.wh64.chain.data.UserStatus
import net.wh64.chain.logout
import net.wh64.chain.ui.CustomInputField

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
fun ApplyButton(onClick: () -> Unit) {
	Button(onClick = onClick) {
		Icon(Icons.Outlined.Check, null)
		Text("변경하기")
	}
}

@Composable
fun Setting(container: ActivityContainer, modifier: Modifier = Modifier) {
	val data = runBlocking { container.user.getMe() } ?: return
	val scope = rememberCoroutineScope()

	val me = remember { mutableStateOf(data) }
	val name = remember { mutableStateOf(me.value.name) }
	val username = remember { mutableStateOf(me.value.username) }
	val password = remember { mutableStateOf("") }

	val dropdown = remember { mutableStateOf(false) }
	val status = listOf("상태 설정", "Online", "Offline", "Idle", "Do Not Disturb")
	val itemPosition = remember {
		mutableStateOf(0)
	}

	Column(modifier = modifier.fillMaxSize().padding(horizontal = 15.dp)) {
		SetInfo {
			CustomInputField(
				placeholder = "닉네임",
				state = name,
				icon = rememberVectorPainter(Icons.Outlined.AccountCircle),
				modifier = Modifier.weight(1f)
			)

			Spacer(modifier = Modifier.width(10.dp))
			ApplyButton {
				scope.launch {
					container.user.updateUser(UpdateForm(name = name.value))
					Toast.makeText(container.ctx, "닉네임을 변경 했어요!", Toast.LENGTH_SHORT).show()
				}
			}
		}
		Spacer(modifier = Modifier.height(10.dp))

		SetInfo {
			CustomInputField(
				placeholder = "아이디",
				state = username,
				icon = rememberVectorPainter(Icons.Outlined.Person),
				modifier = Modifier.weight(1f)
			)

			Spacer(modifier = Modifier.width(10.dp))
			ApplyButton {
				scope.launch {
					container.user.updateUser(UpdateForm(username = username.value))
					Toast.makeText(container.ctx, "아이디를 변경 했어요! 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show()
					delay(300)

					logout(container.act, container.ctx)
				}
			}
		}
		Spacer(modifier = Modifier.height(10.dp))

		SetInfo {
			CustomInputField(
				placeholder = "비밀번호",
				state = password,
				icon = painterResource(R.drawable.password_24px),
				secret = true,
				modifier = Modifier.weight(1f)
			)

			Spacer(modifier = Modifier.width(10.dp))
			ApplyButton {
				scope.launch {
					container.user.updateUser(UpdateForm(password = password.value))
					Toast.makeText(container.ctx, "비밀번호를 변경 했어요! 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show()
					delay(300)

					logout(container.act, container.ctx)
				}
				return@ApplyButton
			}
		}
		Spacer(modifier = Modifier.height(10.dp))

		SetInfo {
			Row(
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.clickable {
					dropdown.value = !dropdown.value
				}
			) {
				Text(text = status[itemPosition.value])
				Image(
					painter = painterResource(id = R.drawable.arrow_drop_down_24px),
					contentDescription = "DropDown Icon"
				)
			}
			DropdownMenu(
				expanded = dropdown.value,
				onDismissRequest = {
					dropdown.value = false
				}
			) {
				DropdownMenuItem(
					text = {
						Text("Online")
					},
					onClick = {
						scope.launch {
							container.user.updateUser(UpdateForm(status = UserStatus.ONLINE))
							Toast.makeText(container.ctx, "상태를 온라인으로 전환 했어요!", Toast.LENGTH_SHORT).show()
						}
					}
				)

				DropdownMenuItem(
					text = {
						Text("Idle")
					},
					onClick = {
						scope.launch {
							container.user.updateUser(UpdateForm(status = UserStatus.IDLE))
							Toast.makeText(container.ctx, "상태를 자리비움으로 전환 했어요!", Toast.LENGTH_SHORT).show()
						}
					}
				)

				DropdownMenuItem(
					text = {
						Text("Do Not Disturb")
					},
					onClick = {
						scope.launch {
							container.user.updateUser(UpdateForm(status = UserStatus.DO_NOT_DISTURB))
							Toast.makeText(container.ctx, "상태를 방해금지로 전환 했어요!", Toast.LENGTH_SHORT).show()
						}
					}
				)

				DropdownMenuItem(
					text = {
						Text("Offline")
					},
					onClick = {
						scope.launch {
							container.user.updateUser(UpdateForm(status = UserStatus.OFFLINE))
							Toast.makeText(container.ctx, "상태를 오프라인으로 전환 했어요!", Toast.LENGTH_SHORT).show()
						}
					}
				)
			}
		}
		Spacer(modifier = Modifier.height(30.dp))

		Row(
			horizontalArrangement = Arrangement.End,
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.fillMaxWidth().height(65.dp)
		) {
			Button(onClick = {
				logout(container.act, container.ctx)
			}) {
				Icon(painterResource(R.drawable.logout_24px), null)
				Text("로그아웃")
			}
		}
	}
}
