package com.kuanluntseng.swipify.data.playback

import com.google.gson.annotations.SerializedName

data class Playback(

	@field:SerializedName("item")
	val item: Item? = null,

	@field:SerializedName("currently_playing_type")
	val currentlyPlayingType: String? = null,

	@field:SerializedName("shuffle_state")
	val shuffleState: Boolean? = null,

	@field:SerializedName("context")
	val context: Any? = null,

	@field:SerializedName("is_playing")
	val isPlaying: Boolean? = null,

	@field:SerializedName("progress_ms")
	val progressMs: Int? = null,

	@field:SerializedName("device")
	val device: Device? = null,

	@field:SerializedName("actions")
	val actions: Actions? = null,

	@field:SerializedName("repeat_state")
	val repeatState: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: Long? = null
)

data class ImagesItem(

	@field:SerializedName("width")
	val width: Int? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("height")
	val height: Int? = null
)

data class Device(

	@field:SerializedName("is_active")
	val isActive: Boolean? = null,

	@field:SerializedName("is_private_session")
	val isPrivateSession: Boolean? = null,

	@field:SerializedName("is_restricted")
	val isRestricted: Boolean? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("volume_percent")
	val volumePercent: Int? = null
)

data class ExternalUrls(

	@field:SerializedName("spotify")
	val spotify: String? = null
)

data class Item(

	@field:SerializedName("disc_number")
	val discNumber: Int? = null,

	@field:SerializedName("album")
	val album: Album? = null,

	@field:SerializedName("available_markets")
	val availableMarkets: List<Any?>? = null,

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
	val previewUrl: Any? = null,

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

	@field:SerializedName("is_local")
	val isLocal: Boolean? = null,

	@field:SerializedName("external_urls")
	val externalUrls: ExternalUrls? = null
)

data class ExternalIds(

	@field:SerializedName("isrc")
	val isrc: String? = null
)

data class Disallows(

	@field:SerializedName("toggling_shuffle")
	val togglingShuffle: Boolean? = null,

	@field:SerializedName("resuming")
	val resuming: Boolean? = null,

	@field:SerializedName("toggling_repeat_context")
	val togglingRepeatContext: Boolean? = null,

	@field:SerializedName("toggling_repeat_track")
	val togglingRepeatTrack: Boolean? = null
)

data class Album(

	@field:SerializedName("images")
	val images: List<ImagesItem?>? = null,

	@field:SerializedName("available_markets")
	val availableMarkets: List<Any?>? = null,

	@field:SerializedName("release_date_precision")
	val releaseDatePrecision: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("uri")
	val uri: String? = null,

	@field:SerializedName("total_tracks")
	val totalTracks: Int? = null,

	@field:SerializedName("artists")
	val artists: List<ArtistsItem?>? = null,

	@field:SerializedName("release_date")
	val releaseDate: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("album_type")
	val albumType: String? = null,

	@field:SerializedName("href")
	val href: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("external_urls")
	val externalUrls: ExternalUrls? = null
)

data class Actions(

	@field:SerializedName("disallows")
	val disallows: Disallows? = null
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
