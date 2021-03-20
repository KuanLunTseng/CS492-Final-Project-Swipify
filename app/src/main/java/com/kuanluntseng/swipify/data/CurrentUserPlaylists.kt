package com.kuanluntseng.swipify.data


import com.google.gson.annotations.SerializedName

data class CurrentUserPlaylists(
    @field:SerializedName("href") val href : String,
    @field:SerializedName("items") val items : List<Items>,
    @field:SerializedName("limit") val limit : Int,
    @field:SerializedName("next") val next : String,
    @field:SerializedName("offset") val offset : Int,
    @field:SerializedName("previous") val previous : String,
    @field:SerializedName("total") val total : Int
    )
data class Items (

    @field:SerializedName("collaborative") val collaborative : Boolean,
    @field:SerializedName("description") val description : String,
    @field:SerializedName("external_urls") val external_urls : External_urls,
    @field:SerializedName("href") val href : String,
    @field:SerializedName("id") val id : String,
    @field:SerializedName("images") val images : List<Images>,
    @field:SerializedName("name") val name : String,
    @field:SerializedName("owner") val owner : Owner,
    @field:SerializedName("primary_color") val primary_color : String,
    @field:SerializedName("public") val public : Boolean,
    @field:SerializedName("snapshot_id") val snapshot_id : String,
    @field:SerializedName("tracks") val tracks : Tracks,
    @field:SerializedName("type") val type : String,
    @field:SerializedName("uri") val uri : String
)
data class Images (

    @field:SerializedName("height") val height : Int,
    @field:SerializedName("url") val url : String,
    @field:SerializedName("width") val width : Int
)
data class External_urls (

    @field:SerializedName("spotify") val spotify : String
)
data class Tracks (

    @field:SerializedName("href") val href : String,
    @field:SerializedName("total") val total : Int
)
data class Owner (

    @field:SerializedName("display_name") val display_name : String,
    @field:SerializedName("external_urls") val external_urls : External_urls,
    @field:SerializedName("href") val href : String,
    @field:SerializedName("id") val id : String,
    @field:SerializedName("type") val type : String,
    @field:SerializedName("uri") val uri : String
)
