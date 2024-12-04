package net.wh64.chain.controller

import android.content.Context
import android.widget.Toast
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import net.wh64.chain.R
import net.wh64.chain.data.UserStatus
import net.wh64.chain.util.client

@Serializable
data class LoginForm(
	val username: String,
	val password: String
)

@Serializable
data class LoginResponse(val token: String)

@Serializable
data class RegisterForm(
	val name: String,
	val username: String,
	val password: String
)

@Serializable
data class ChainUser(
	val name: String,
	val username: String,
	val status: UserStatus,
	val createdAt: Long
)

class UserController(private val token: String, private val ctx: Context) {
	suspend fun getMe(): ChainUser? {
		val res = client.get(
			"${ctx.resources.getString(R.string.api_url)}/auth/me"
		) {
			header("Authorization", "Basic $token")
		}

		return res.body()
	}

	companion object {
		suspend fun login(ctx: Context, form: LoginForm): String? {
			val res = client.post(
				"${ctx.getString(R.string.api_url)}/auth/login"
			) {
				headers["Content-Type"] = "application/json"
				setBody(form)
			}

			if (res.status != HttpStatusCode.OK) {
				return null
			}

			Toast.makeText(ctx, "Login Success!", Toast.LENGTH_LONG).show()
			val body = res.body<LoginResponse>()

			return body.token
		}

		suspend fun register(ctx: Context, form: RegisterForm) {
			val res = client.post(
				"${ctx.getString(R.string.api_url)}/auth/signup"
			) {
				headers["Content-Type"] = "application/json"
				setBody(form)
			}

			if (res.status != HttpStatusCode.Created && res.status != HttpStatusCode.OK) {
				throw Exception("Register Failed!")
			}
		}
	}
}