package com.kuanluntseng.swipify.data.newplaylist

import com.google.gson.annotations.SerializedName

data class NewPlaylist(

	@field:SerializedName("owner")
	val owner: Owner? = null,

	@field:SerializedName("images")
	val images: List<Any?>? = null,

	@field:SerializedName("snapshot_id")
	val snapshotId: String? = null,

	@field:SerializedName("collaborative")
	val collaborative: Boolean? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("primary_color")
	val primaryColor: Any? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("uri")
	val uri: String? = null,

	@field:SerializedName("tracks")
	val tracks: Tracks? = null,

	@field:SerializedName("followers")
	val followers: Followers? = null,

	@field:SerializedName("public")
	val jsonMemberPublic: Boolean? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("href")
	val href: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("external_urls")
	val externalUrls: ExternalUrls? = null
)

data class ExternalUrls(

	@field:SerializedName("spotify")
	val spotify: String? = null
)

data class Owner(

	@field:SerializedName("href")
	val href: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("display_name")
	val displayName: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("external_urls")
	val externalUrls: ExternalUrls? = null,

	@field:SerializedName("uri")
	val uri: String? = null
)

data class Tracks(

	@field:SerializedName("next")
	val next: Any? = null,

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("offset")
	val offset: Int? = null,

	@field:SerializedName("previous")
	val previous: Any? = null,

	@field:SerializedName("limit")
	val limit: Int? = null,

	@field:SerializedName("href")
	val href: String? = null,

	@field:SerializedName("items")
	val items: List<Any?>? = null
)

data class Followers(

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("href")
	val href: Any? = null
)
