package jie.wen.transurbancodetest.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import jie.wen.transurbancodetest.model.SortByField
import jie.wen.transurbancodetest.model.dto.StarShipDataDTO
import jie.wen.transurbancodetest.model.dto.StarShipsListResponseDTO
import jie.wen.transurbancodetest.other.Resource
import jie.wen.transurbancodetest.repository.StarshipDataRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test
import retrofit2.Response
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class StarshipsListViewModelTest {
    var testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Test
    fun testFetchStarshipListDataSuccess() {
        val repository : StarshipDataRepository = mockk()
        val response = StarShipsListResponseDTO()
        response.next = "https://swapi.dev/api/starships/?page=2&format=api"
        val success = Response.success(200, response)
        val starshipsListViewModel = StarshipsListViewModel(repository)
        // success case

        val page = 1
        starshipsListViewModel.currentPage.set(1)

        coEvery { repository.fetchStarshipsListData(page) } returns success
        testDispatcher.runBlockingTest {
            starshipsListViewModel.fetchStarshipsListData(page)
            Assert.assertEquals(2, starshipsListViewModel.currentPage.get())
            Assert.assertEquals(Resource.success(response), starshipsListViewModel.starshipsListResponseDTOLiveData.value)
        }

        coVerify(exactly = 1) { repository.fetchStarshipsListData(page) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testFetchStarshipListDataFailPage() {
        val repository : StarshipDataRepository = mockk()
        val errorResponse : ResponseBody = ResponseBody.create(null, ByteArray(0))
        val starshipsListViewModel = StarshipsListViewModel(repository)
        val failResponse = Response.error<StarShipsListResponseDTO>(400, errorResponse)

        // failed case
        val page = 1
        starshipsListViewModel.currentPage.set(page)

        coEvery { repository.fetchStarshipsListData(page) } returns failResponse
        testDispatcher.runBlockingTest {
            starshipsListViewModel.fetchStarshipsListData(page)
            Assert.assertEquals(Resource.error(failResponse.code().toString(), null), starshipsListViewModel.starshipsListResponseDTOLiveData.value)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testFetchStarshipListDataInvalidPage() {
        val repository : StarshipDataRepository = mockk()
        val starshipsListViewModel = StarshipsListViewModel(repository)

        val page = 1
        starshipsListViewModel.currentPage.set(0)

        testDispatcher.runBlockingTest {
            starshipsListViewModel.fetchStarshipsListData(page)
            Assert.assertEquals(0, starshipsListViewModel.currentPage.get())
        }
    }

    @Test
    fun testGetCurrentPage() {
        val repository : StarshipDataRepository = mockk()
        val starshipsListViewModel = StarshipsListViewModel(repository)
        assertEquals(2, starshipsListViewModel.getCurrentPage("https://swapi.dev/api/starships/?page=2&format=api"))
        assertEquals(1, starshipsListViewModel.getCurrentPage("https://swapi.dev/api/starships/?page=1"))
        assertEquals(1, starshipsListViewModel.getCurrentPage("https://swapi.dev/api/starships/"))
        assertEquals(1, starshipsListViewModel.getCurrentPage("https://swapi.dev/api/starships/?page=adasda"))
        assertEquals(1, starshipsListViewModel.getCurrentPage(null))
    }

    @Test
    fun testSortStarshipList() {
        val repository : StarshipDataRepository = mockk()
        val starshipsListViewModel = StarshipsListViewModel(repository)

        val formatter = DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'").toFormatter()
        val starshipDTO1 = StarShipDataDTO()
        starshipDTO1.name = "aa"
        starshipDTO1.model = "bb"
        starshipDTO1.manufacturer = "cc"

        starshipDTO1.created = Date.from(LocalDate.parse("2014-12-10T16:36:50.509000Z", formatter).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
        starshipDTO1.edited = Date.from(LocalDate.parse("2014-12-11T16:36:50.509000Z", formatter).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())

        val starshipDTO2 = StarShipDataDTO()
        starshipDTO2.name = "cc"
        starshipDTO2.model = "aa"
        starshipDTO2.manufacturer = "aa"
        starshipDTO2.created = Date.from(LocalDate.parse("2014-12-09T16:36:50.509000Z", formatter).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
        starshipDTO2.edited = Date.from(LocalDate.parse("2014-12-12T16:26:50.509000Z", formatter).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())

        val starshipDTO3 = StarShipDataDTO()
        starshipDTO3.name = "bb"
        starshipDTO3.model = "cc"
        starshipDTO3.manufacturer = "bb"
        starshipDTO3.created = Date.from(LocalDate.parse("2014-12-11T16:36:50.509000Z", formatter).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
        starshipDTO3.edited = Date.from(LocalDate.parse("2014-12-12T16:46:50.509000Z", formatter).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())

        val testData = arrayListOf(starshipDTO1, starshipDTO2, starshipDTO3)

        val result1 = starshipsListViewModel.sortStarshipList(testData, SortByField.NAME.name, false)

        assertEquals(starshipDTO1.name, result1[0].name)
        assertEquals(starshipDTO3.name, result1[1].name)
        assertEquals(starshipDTO2.name, result1[2].name)

        val result2 = starshipsListViewModel.sortStarshipList(testData, SortByField.MODEL.name, false)

        assertEquals(starshipDTO2.model, result2[0].model)
        assertEquals(starshipDTO1.model, result2[1].model)
        assertEquals(starshipDTO3.model, result2[2].model)

        val result3 = starshipsListViewModel.sortStarshipList(testData, SortByField.MODEL.name, true)

        assertEquals(starshipDTO3.model, result3[0].model)
        assertEquals(starshipDTO1.model, result3[1].model)
        assertEquals(starshipDTO2.model, result3[2].model)

        val result4 = starshipsListViewModel.sortStarshipList(testData, SortByField.MANUFACTURER.name, false)

        assertEquals(starshipDTO2.manufacturer, result4[0].manufacturer)
        assertEquals(starshipDTO3.manufacturer, result4[1].manufacturer)
        assertEquals(starshipDTO1.manufacturer, result4[2].manufacturer)

        val result5 = starshipsListViewModel.sortStarshipList(testData, SortByField.EDITED_TIME.name, false)

        assertEquals(starshipDTO1.edited, result5[0].edited)
        assertEquals(starshipDTO2.edited, result5[1].edited)
        assertEquals(starshipDTO3.edited, result5[2].edited)

        val result6 = starshipsListViewModel.sortStarshipList(testData, SortByField.CREATED_TIME.name, false)

        assertEquals(starshipDTO2.created, result6[0].created)
        assertEquals(starshipDTO1.created, result6[1].created)
        assertEquals(starshipDTO3.created, result6[2].created)

        val result7 = starshipsListViewModel.sortStarshipList(testData, SortByField.CREATED_TIME.name, true)

        assertEquals(starshipDTO3.created, result7[0].created)
        assertEquals(starshipDTO1.created, result7[1].created)
        assertEquals(starshipDTO2.created, result7[2].created)
    }

    @Test
    fun testChangeListBySortOption() {
        val repository : StarshipDataRepository = mockk()
        val starshipsListViewModel = StarshipsListViewModel(repository)

        val formatter = DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'").toFormatter()
        val starshipDTO1 = StarShipDataDTO()
        starshipDTO1.name = "aa"
        starshipDTO1.model = "bb"
        starshipDTO1.manufacturer = "cc"

        starshipDTO1.created = Date.from(LocalDate.parse("2014-12-10T16:36:50.509000Z", formatter).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
        starshipDTO1.edited = Date.from(LocalDate.parse("2014-12-11T16:36:50.509000Z", formatter).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())

        val starshipDTO2 = StarShipDataDTO()
        starshipDTO2.name = "cc"
        starshipDTO2.model = "aa"
        starshipDTO2.manufacturer = "aa"
        starshipDTO2.created = Date.from(LocalDate.parse("2014-12-09T16:36:50.509000Z", formatter).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
        starshipDTO2.edited = Date.from(LocalDate.parse("2014-12-12T16:26:50.509000Z", formatter).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())

        val starshipDTO3 = StarShipDataDTO()
        starshipDTO3.name = "bb"
        starshipDTO3.model = "cc"
        starshipDTO3.manufacturer = "bb"
        starshipDTO3.created = Date.from(LocalDate.parse("2014-12-11T16:36:50.509000Z", formatter).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
        starshipDTO3.edited = Date.from(LocalDate.parse("2014-12-12T16:46:50.509000Z", formatter).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())

        val testData = arrayListOf(starshipDTO1, starshipDTO2, starshipDTO3)

        starshipsListViewModel.changeListBySortOption(testData, SortByField.MANUFACTURER.name, true)
        val actualList = starshipsListViewModel.sortStarshipList(testData, SortByField.MANUFACTURER.name, true)
        Assert.assertEquals(Resource.switchList(actualList), starshipsListViewModel.starshipsListDataDTOLiveData.value)
        Assert.assertEquals(Resource.finishLoading(null), starshipsListViewModel.starshipsListResponseDTOLiveData.value)
    }
}