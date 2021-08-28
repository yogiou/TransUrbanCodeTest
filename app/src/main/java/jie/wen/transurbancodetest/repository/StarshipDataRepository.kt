package jie.wen.transurbancodetest.repository

import jie.wen.transurbancodetest.api.ApiHelper
import jie.wen.transurbancodetest.other.Constants.Companion.API_FORMAT
import javax.inject.Inject

class StarshipDataRepository @Inject constructor(
    private val apiHelper: ApiHelper
) {
    suspend fun fetchStarshipsListData(page: Int) = apiHelper.fetchStarShipListData(page, format = API_FORMAT)
}