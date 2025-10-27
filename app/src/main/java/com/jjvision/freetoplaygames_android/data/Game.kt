package com.jjvision.freetoplaygames_android.data

import com.google.gson.annotations.SerializedName

data class Game(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("short_description") val descriptionShort: String,
    @SerializedName("description") val description: String,
    @SerializedName("game_url") val urlGame: String,
    @SerializedName("genre") val genre: String,
    @SerializedName("platform") val platform: String,
    @SerializedName("developer") val developer: String,
    @SerializedName("freetogame_profile_url") val profileUrl: String,
    @SerializedName("minimum_system_requirements") val minimumSystemRequirements: MinimumSystemRequirements,
    @SerializedName("screenshots") val screenshots: List<Screenshots>
)

data class MinimumSystemRequirements(
    @SerializedName("os") val os: String,
    @SerializedName("processor") val processor: String,
    @SerializedName("memory") val memory: String,
    @SerializedName("graphics") val graphics: String,
    @SerializedName("storage") val storage: String
)

data class Screenshots(
    @SerializedName("image") val image: String
)
