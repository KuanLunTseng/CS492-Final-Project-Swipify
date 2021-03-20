package com.kuanluntseng.swipify.data.userprofile

import com.google.gson.annotations.SerializedName

data class UserProfile(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("images")
	val images: List<ImagesItem?>? = null,

	@field:SerializedName("product")
	val product: String? = null,

	@field:SerializedName("followers")
	val followers: Followers? = null,

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
	val uri: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)

data class Followers(

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("href")
	val href: Any? = null
)

data class ExternalUrls(

	@field:SerializedName("spotify")
	val spotify: String? = null
)

data class ImagesItem(

	@field:SerializedName("width")
	val width: Any? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("height")
	val height: Any? = null
)
