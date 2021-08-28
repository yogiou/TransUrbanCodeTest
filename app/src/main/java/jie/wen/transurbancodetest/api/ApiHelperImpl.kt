package jie.wen.transurbancodetest.api

import jie.wen.transurbancodetest.model.dto.StarShipsListResponseDTO
import jie.wen.transurbancodetest.other.Constants.Companion.API_FORMAT
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService
): ApiHelper{
    override suspend fun fetchStarShipListData(page: Int, format: String): Response<StarShipsListResponseDTO> = apiService.fetchStarShipListData(page, format = API_FORMAT)
}