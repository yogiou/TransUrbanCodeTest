package jie.wen.transurbancodetest.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import jie.wen.transurbancodetest.MainActivity
import jie.wen.transurbancodetest.R
import jie.wen.transurbancodetest.databinding.FragmentStarshipListBinding
import jie.wen.transurbancodetest.model.SortByField
import jie.wen.transurbancodetest.model.dto.StarShipDataDTO
import jie.wen.transurbancodetest.model.dto.StarShipsListResponseDTO
import jie.wen.transurbancodetest.other.Resource
import jie.wen.transurbancodetest.other.Status
import jie.wen.transurbancodetest.ui.adapter.StarshipsListAdapter
import jie.wen.transurbancodetest.ui.adapter.StarshipsListAdapter.Companion.UPDATE_CHECK_BOX
import jie.wen.transurbancodetest.viewmodel.StarshipsListViewModel

@AndroidEntryPoint
class StarShipsListFragment : Fragment(), StarshipsListAdapter.OnItemClickListener, AdapterView.OnItemSelectedListener {
    private lateinit var starshipsListAdapter: StarshipsListAdapter
    private lateinit var binding: FragmentStarshipListBinding
    private lateinit var viewModel: StarshipsListViewModel
    private val starshipsListResponseObserver = Observer<Resource<StarShipsListResponseDTO>> { response ->
        when (response?.status) {
            Status.SUCCESS -> {
                response.data?.results?.takeIf { it.isNotEmpty() }?.let { starshipsList ->
                    val pos = starshipsListAdapter.itemCount
                    starshipsListAdapter.items.addAll(starshipsList)
                    starshipsListAdapter.notifyItemRangeInserted(pos, starshipsList.size)
                    starshipsListAdapter.itemsCopy.addAll(starshipsList)
                }

                if (response.data?.count == starshipsListAdapter.itemCount) {
                    binding.sortSpinner.visibility = View.VISIBLE
                }

                viewModel.starshipsListResponseDTOLiveData.postValue(Resource.finishLoading(null))
            }
            Status.LOADING -> {
                binding.loadingIndicator.visibility = View.VISIBLE
            }
            Status.FINISH_LOADING -> {
                binding.loadingIndicator.visibility = View.GONE
            }
            Status.ERROR -> {
                response.message?.let { msg ->
                    Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
                }
                viewModel.starshipsListResponseDTOLiveData.postValue(Resource.finishLoading(null))

                showRetryButton()
            }
            else -> {
                binding.loadingIndicator.visibility = View.GONE
            }
        }
    }

    private val starshipsListDTOObserver = Observer<Resource<List<StarShipDataDTO>>> { response ->
        when (response?.status) {
            Status.SWITCH_LIST -> {
                response.data?.takeIf { it.isNotEmpty() }?.let { starshipsList ->
                    starshipsListAdapter.items.clear()
                    starshipsListAdapter.items.addAll(starshipsList)
                    starshipsListAdapter.notifyItemRangeChanged(0, starshipsList.size)
                }

                viewModel.starshipsListResponseDTOLiveData.postValue(Resource.finishLoading(null))
            }
            else -> {
                binding.loadingIndicator.visibility = View.GONE
            }
        }
    }

    companion object {
        fun newInstance() = StarShipsListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentStarshipListBinding.inflate(layoutInflater)
        starshipsListAdapter = StarshipsListAdapter(this)
        binding.starshipsListView.run {
            if (adapter == null) {
                adapter = starshipsListAdapter
                addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
                layoutManager = LinearLayoutManager(context)
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (!recyclerView.canScrollVertically(1) && dy > 0
                            && starshipsListAdapter.itemCount > 0) {
                            //scrolled to BOTTOM
                            loadStarshipsList()
                        }
                    }
                })
            }
        }

        activity?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.sort_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                binding.sortSpinner.adapter = adapter
                binding.sortSpinner.onItemSelectedListener = this
            }
        }

        viewModel = ViewModelProvider(this).get(StarshipsListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        viewModel.starshipsListResponseDTOLiveData.observe(viewLifecycleOwner, starshipsListResponseObserver)
        viewModel.starshipsListDataDTOLiveData.observe(viewLifecycleOwner, starshipsListDTOObserver)

        parentFragmentManager.setFragmentResultListener(MainActivity.POS_KEY, viewLifecycleOwner) { requestKey, bundle ->
            bundle.getInt(requestKey).let {
                starshipsListAdapter.notifyItemChanged(it, UPDATE_CHECK_BOX)
            }
        }

        (activity as? MainActivity)?.setActionBarTitle(resources.getString(R.string.starships_list))
        (activity as? MainActivity)?.showActionBarBackButton(false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (starshipsListAdapter.itemCount == 0 && binding.starshipsListView.isEnabled) {
            loadStarshipsList()
        }
    }

    override fun onItemClicked(starShipDataDTO: StarShipDataDTO, position: Int) {
        (activity as? MainActivity)?.goToStarShipDetailsFragment(starShipDataDTO, position)
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        when (pos) {
            1 -> viewModel.changeListBySortOption(starshipsListAdapter.itemsCopy, SortByField.NAME.name, false)
            2 -> viewModel.changeListBySortOption(starshipsListAdapter.itemsCopy, SortByField.MODEL.name, false)
            3 -> viewModel.changeListBySortOption(starshipsListAdapter.itemsCopy, SortByField.MANUFACTURER.name, false)
            else -> {
                starshipsListAdapter.items.clear()
                starshipsListAdapter.items.addAll(starshipsListAdapter.itemsCopy)
                starshipsListAdapter.notifyItemRangeChanged(0, starshipsListAdapter.itemsCopy.size)
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) { }

    private fun loadStarshipsList() {
        (activity as? MainActivity)?.let {
            context?.let { context ->
                if (it.isOnline(context)) {
                    viewModel.makeApiCall()
                }
            }
        }
    }

    private fun showRetryButton() {
        if (starshipsListAdapter.itemCount == 0) {
            binding.retryBtn.visibility = View.VISIBLE
            binding.retryBtn.setOnClickListener {
                binding.retryBtn.visibility = View.GONE
                loadStarshipsList()
            }
        }
    }
}