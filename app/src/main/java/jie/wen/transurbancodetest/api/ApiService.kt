package jie.wen.transurbancodetest.api

import jie.wen.transurbancodetest.model.dto.StarShipsListResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("starships")
    suspend fun fetchStarShipListData(
        @Query("page") page: Int,
        @Query("format") format: String
    ) : Response<StarShipsListResponseDTO>
}