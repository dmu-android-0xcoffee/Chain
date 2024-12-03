package net.wh64.chain.data

import kotlinx.serialization.Serializable

@Serializable
data class ChainUser(
	val name: String,
	val username: String,
	val status: UserStatus,
	val createdAt: Long
)
