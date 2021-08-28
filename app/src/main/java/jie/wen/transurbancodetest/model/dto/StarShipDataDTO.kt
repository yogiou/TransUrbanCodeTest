package jie.wen.transurbancodetest.model.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class StarShipDataDTO (
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("model")
    var model: String? = null,
    @SerializedName("manufacturer")
    var manufacturer: String? = null,
    @SerializedName("cost_in_credits")
    var costInCredits: String? = null,
    @SerializedName("length")
    var length: String? = null,
    @SerializedName("max_atmosphering_speed")
    var maxAtmospheringSpeed: String? = null,
    @SerializedName("crew")
    var crew: String? = null,
    @SerializedName("passengers")
    var passengers: String? = null,
    @SerializedName("cargo_capacity")
    var cargoCapacity: String? = null,
    @SerializedName("consumables")
    var consumables: String? = null,
    @SerializedName("hyperdrive_rating")
    var hyperDriveRating: String? = null,
    @SerializedName("MGLT")
    var mglt: String? = null,
    @SerializedName("starship_class")
    var starshipClass: String? = null,
    @SerializedName("pilots")
    var pilots: List<String>? = null,
    @SerializedName("films")
    var films: List<String>? = null,
    @SerializedName("created")
    var created: Date? = null,
    @SerializedName("edited")
    var edited: Date? = null,
    @SerializedName("url")
    var url: String? = null
): Serializable