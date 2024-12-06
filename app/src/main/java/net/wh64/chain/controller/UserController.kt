package net.wh64.chain.controller

import android.content.Context
import android.util.Log
import android.widget.Toast
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import net.wh64.chain.R
import net.wh64.chain.data.ChainUser
import net.wh64.chain.data.FriendRef
import net.wh64.chain.data.Friends
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
data class UpdateForm(
	val name: String? = null,
	val username: String? = null,
	val status: UserStatus? = null,
	val password: String? = null,
)

@Serializable
class LocationData(
	val lng: Double,
	val lat: Double
)

@Serializable
data class ChainPayload<T>(
	val action: String,
	val data: T,
)

@Serializable
class LocationDataResp(
	val target: String,
	val lng: Double,
	val lat: Double,
	val loggedAt: Long
)

@Serializable
data class ChainChannelData(
	val id: Int,
	val creator: ChainUser,
	val users: ChainUser
)

@Serializable
data class CreateForm(
	val target: String
)

@Serializable
data class IdFallback(
	val id: Int,
)

class UserController(val token: String, private val ctx: Context) {
	suspend fun getMe(): ChainUser? {
		val res = client.get(
			"${ctx.resources.getString(R.string.api_url)}/auth/me"
		) {
			header("Authorization", "Basic $token")
		}

		return res.body()
	}

	suspend fun saveLocation(loc: LocationData) {
		client.patch(
			"${ctx.resources.getString(R.string.api_url)}/api/user/location"
		) {
			header("Authorization", "Basic $token")
			header("Content-Type", "application/json")

			setBody(loc)
		}
	}

	suspend fun getFriends(): Friends {
		val res = client.get(
			"${ctx.resources.getString(R.string.api_url)}/api/user/friends"
		) {
			header("Authorization", "Basic $token")
		}

		return res.body()
	}

	suspend fun getChannel(tid: String): ChainChannelData? {
		val res = client.get(
			"${ctx.resources.getString(R.string.api_url)}/api/channel/pair/${tid}"
		) {
			header("Authorization", "Basic $token")
		}

		if (res.status != HttpStatusCode.OK) {
			return null
		}

		return res.body()
	}

	suspend fun getChannels(): List<ChainChannelData> {
		val res = client.get(
			"${ctx.resources.getString(R.string.api_url)}/api/channel/available"
		) {
			header("Authorization", "Basic $token")
		}

		return res.body()
	}

	suspend fun createChannel(target: String): Boolean {
		val res = client.post("${ctx.resources.getString(R.string.api_url)}/api/channel/create") {
			header("Authorization", "Basic $token")
			contentType(ContentType.Application.Json)
			setBody(CreateForm(target))
		}

		Log.d("status", res.status.value.toString())

		return res.status == HttpStatusCode.Created
	}

	suspend fun getFriendLocations(): List<LocationDataResp> {
		val res = client.get(
			"${ctx.resources.getString(R.string.api_url)}/api/user/location"
		) {
			header("Authorization", "Basic $token")
		}

		return res.body()
	}

	suspend fun updateUser(form: UpdateForm): Boolean {
		val res = client.put("${ctx.getString(R.string.api_url)}/api/user") {
			header("Authorization", "Basic $token")
			header("Content-Type", "application/json")
			setBody(form)
		}

		return res.status == HttpStatusCode.OK
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