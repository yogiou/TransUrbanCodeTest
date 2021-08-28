package jie.wen.transurbancodetest.api

import jie.wen.transurbancodetest.model.dto.StarShipsListResponseDTO
import retrofit2.Response

interface ApiHelper {
    suspend fun fetchStarShipListData(page: Int, format: String):Response<StarShipsListResponseDTO>
}