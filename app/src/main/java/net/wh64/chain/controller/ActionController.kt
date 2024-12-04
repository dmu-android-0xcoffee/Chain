package net.wh64.chain.controller

import android.app.Activity.MODE_PRIVATE
import android.content.Context

class ActionController(ctx: Context) {
	private val login = ctx.getSharedPreferences("login", MODE_PRIVATE)

	fun getToken(): String? {
		return login.getString("token", null)
	}

	fun injectToken(token: String): Unit = with(login.edit()) {
		putString("token", token)
		commit()
	}

	fun deleteToken(): Unit = with(login.edit()) {
		remove("token")
		commit()
	}
}