package net.wh64.chain.data

import kotlinx.serialization.Serializable

@Serializable
data class Friends(val friends: List<FriendRef>)

@Serializable
data class FriendRef(
	val fid: String,
	val accept: Boolean,
	val data: ChainUser,
)

@Serializable
data class ChainUser(
	val name: String,
	val username: String,
	val status: UserStatus,
	val createdAt: Long
)
