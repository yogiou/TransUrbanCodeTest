package jie.wen.transurbancodetest.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jie.wen.transurbancodetest.model.SortByField
import jie.wen.transurbancodetest.model.dto.StarShipDataDTO
import jie.wen.transurbancodetest.model.dto.StarShipsListResponseDTO
import jie.wen.transurbancodetest.other.Constants.Companion.API_CALL_TIMEOUT
import jie.wen.transurbancodetest.other.Constants.Companion.PAGE_PREFIX
import jie.wen.transurbancodetest.other.Constants.Companion.TIME_OUT_MESSAGE
import jie.wen.transurbancodetest.other.Resource
import jie.wen.transurbancodetest.repository.StarshipDataRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

@HiltViewModel
class StarshipsListViewModel @Inject constructor(private val repository: StarshipDataRepository) : ViewModel() {
    var currentPage : AtomicInteger = AtomicInteger(1)

    val starshipsListResponseDTOLiveData: MutableLiveData<Resource<StarShipsListResponseDTO>> by lazy {
        MutableLiveData<Resource<StarShipsListResponseDTO>>()
    }

    val starshipsListDataDTOLiveData: MutableLiveData<Resource<List<StarShipDataDTO>>> by lazy {
        MutableLiveData<Resource<List<StarShipDataDTO>>>()
    }

    override fun onCleared() {
        viewModelScope.takeIf { it.isActive }?.cancel()
        super.onCleared()
    }

    fun makeApiCall() = viewModelScope.launch {
        starshipsListResponseDTOLiveData.postValue(Resource.loading(null))

        val result = withTimeoutOrNull(API_CALL_TIMEOUT) {
            fetchStarshipsListData(currentPage.get())
        }

        if (result == null) {
            starshipsListResponseDTOLiveData.postValue(Resource.error(TIME_OUT_MESSAGE, null))
        }
    }

    suspend fun fetchStarshipsListData(page: Int) {
        if (currentPage.get() > 0) {
            repository.fetchStarshipsListData(page).let {
                if (it.isSuccessful) {
                    starshipsListResponseDTOLiveData.postValue(Resource.success(it.body()))

                    it.body()?.next?.let { next ->
                        currentPage.set(getCurrentPage(next))
                    } ?: apply {
                        currentPage.set(-1)
                    }
                } else {
                    starshipsListResponseDTOLiveData.postValue(Resource.error(it.code().toString(), null))
                }
            }
        } else {
            starshipsListResponseDTOLiveData.postValue(Resource.finishLoading(null))
        }
    }

    fun changeListBySortOption(starShipList: List<StarShipDataDTO>, sortBy: String, reverse: Boolean) {
        starshipsListResponseDTOLiveData.postValue(Resource.loading(null))
        val list = sortStarshipList(starShipList, sortBy, reverse)
        starshipsListDataDTOLiveData.postValue(Resource.switchList(list))
        starshipsListResponseDTOLiveData.postValue(Resource.finishLoading(null))
    }

    // retrieve the page number from 'next' field
    fun getCurrentPage(url: String?): Int {
        val regex = ("$PAGE_PREFIX[0-9]+").toRegex()
        return url?.let {
            regex.find(it)?.value?.removePrefix(PAGE_PREFIX)
        }?.toInt() ?: 1
    }

    // we can extend to sort by other fields and if with reverse order
    fun sortStarshipList(starShipList: List<StarShipDataDTO>, sortBy: String, reverse: Boolean): MutableList<StarShipDataDTO> {
        val clonedList = mutableListOf<StarShipDataDTO>().apply { addAll(starShipList) }

        when (sortBy) {
            SortByField.NAME.name -> clonedList.sortWith(compareBy { it.name?.uppercase() })
            SortByField.MODEL.name -> clonedList.sortWith(compareBy { it.model?.uppercase() })
            SortByField.MANUFACTURER.name -> clonedList.sortWith(compareBy { it.manufacturer?.uppercase() })
            SortByField.CREATED_TIME.name -> clonedList.sortWith(compareBy { it.created })
            SortByField.EDITED_TIME.name -> clonedList.sortWith(compareBy { it.edited })
        }

        if (reverse) {
            clonedList.reverse()
        }

        return clonedList
    }
}