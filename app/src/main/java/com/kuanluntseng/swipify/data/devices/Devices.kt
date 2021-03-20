package com.kuanluntseng.swipify.data.devices

import com.google.gson.annotations.SerializedName

data class Devices(

	@field:SerializedName("devices")
	val devices: List<DevicesItem?>? = null
)

data class DevicesItem(

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
