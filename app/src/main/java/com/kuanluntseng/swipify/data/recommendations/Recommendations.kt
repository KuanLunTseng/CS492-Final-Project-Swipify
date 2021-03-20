package com.kuanluntseng.swipify.data.recommendations

import com.google.gson.annotations.SerializedName

data class Recommendations(

	@field:SerializedName("seeds")
	val seeds: List<SeedsItem?>? = null,

	@field:SerializedName("tracks")
	val tracks: MutableList<TracksItem?>? = null
)

data class TracksItem(

	@field:SerializedName("disc_number")
	val discNumber: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("uri")
	val uri: String? = null,

	@field:SerializedName("duration_ms")
	val durationMs: Int? = null,

	@field:SerializedName("explicit")
	val explicit: Boolean? = null,

	@field:SerializedName("is_playable")
	val isPlayable: Boolean? = null,

	@field:SerializedName("artists")
	val artists: List<ArtistsItem?>? = null,

	@field:SerializedName("preview_url")
	val previewUrl: String? = null,

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

data class ExternalUrls(

	@field:SerializedName("spotify")
	val spotify: String? = null
)

data class SeedsItem(

	@field:SerializedName("href")
	val href: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("initialPoolSize")
	val initialPoolSize: Int? = null,

	@field:SerializedName("afterRelinkingSize")
	val afterRelinkingSize: Int? = null,

	@field:SerializedName("afterFilteringSize")
	val afterFilteringSize: Int? = null
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
