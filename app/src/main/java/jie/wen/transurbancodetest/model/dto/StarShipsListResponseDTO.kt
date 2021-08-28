package jie.wen.transurbancodetest.model.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StarShipsListResponseDTO (
    @SerializedName("count")
    var count: Int = 0,
    @SerializedName("next")
    var next: String? = null,
    @SerializedName("previous")
    var previous: String? = null,
    @SerializedName("results")
    var results: List<StarShipDataDTO>? = null
): Serializable