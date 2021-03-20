package com.kuanluntseng.swipify.data.playlist

import com.google.gson.annotations.SerializedName

data class Playlist(

	@field:SerializedName("owner")
	val owner: Owner? = null,

	@field:SerializedName("images")
	val images: List<ImagesItem?>? = null,

	@field:SerializedName("snapshot_id")
	val snapshotId: String? = null,

	@field:SerializedName("collaborative")
	val collaborative: Boolean? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("uri")
	val uri: String? = null,

	@field:SerializedName("tracks")
	val tracks: Tracks? = null,

	@field:SerializedName("followers")
	val followers: Followers? = null,

	@field:SerializedName("public")
	val jsonMemberPublic: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("href")
	val href: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("external_urls")
	val externalUrls: ExternalUrls? = null
)

data class ImagesItem(

	@field:SerializedName("width")
	val width: Int? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("height")
	val height: Int? = null
)

data class Album(

	@field:SerializedName("images")
	val images: List<ImagesItem?>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("available_markets")
	val availableMarkets: List<String?>? = null,

	@field:SerializedName("album_type")
	val albumType: String? = null,

	@field:SerializedName("href")
	val href: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("external_urls")
	val externalUrls: ExternalUrls? = null,

	@field:SerializedName("uri")
	val uri: String? = null
)

data class Followers(

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("href")
	val href: Any? = null
)

data class Track(

	@field:SerializedName("disc_number")
	val discNumber: Int? = null,

	@field:SerializedName("album")
	val album: Album? = null,

	@field:SerializedName("available_markets")
	val availableMarkets: List<String?>? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("external_ids")
	val externalIds: ExternalIds? = null,

	@field:SerializedName("uri")
	val uri: String? = null,

	@field:SerializedName("duration_ms")
	val durationMs: Int? = null,

	@field:SerializedName("explicit")
	val explicit: Boolean? = null,

	@field:SerializedName("artists")
	val artists: List<ArtistsItem?>? = null,

	@field:SerializedName("preview_url")
	val previewUrl: String? = null,

	@field:SerializedName("popularity")
	val popularity: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("track_number")
	val trackNumber: Int? = null,

	@field:SerializedName("href")
	val href: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("external_urls")
	val externalUrls: ExternalUrls? = null
)

data class ArtistsItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("href")
	val href: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("external_urls")
	val externalUrls: ExternalUrls? = null,

	@field:SerializedName("uri")
	val uri: String? = null
)

data class AddedBy(

	@field:SerializedName("href")
	val href: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("external_urls")
	val externalUrls: ExternalUrls? = null,

	@field:SerializedName("uri")
	val uri: String? = null
)

data class ExternalUrls(

	@field:SerializedName("spotify")
	val spotify: String? = null
)

data class ItemsItem(

	@field:SerializedName("added_at")
	val addedAt: String? = null,

	@field:SerializedName("added_by")
	val addedBy: AddedBy? = null,

	@field:SerializedName("is_local")
	val isLocal: Boolean? = null,

	@field:SerializedName("track")
	val track: Track? = null
)

data class ExternalIds(

	@field:SerializedName("isrc")
	val isrc: String? = null
)

data class Tracks(

	@field:SerializedName("next")
	val next: String? = null,

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
	val items: List<ItemsItem?>? = null
)

data class Owner(

	@field:SerializedName("href")
	val href: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("external_urls")
	val externalUrls: ExternalUrls? = null,

	@field:SerializedName("uri")
	val uri: String? = null
)
