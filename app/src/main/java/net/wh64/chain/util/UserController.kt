package net.wh64.chain.util

import android.content.Context
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import net.wh64.chain.R
import net.wh64.chain.data.UserStatus

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
}