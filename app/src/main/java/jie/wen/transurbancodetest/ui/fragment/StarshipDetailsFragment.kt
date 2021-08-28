package jie.wen.transurbancodetest.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import jie.wen.transurbancodetest.MainActivity
import jie.wen.transurbancodetest.R
import jie.wen.transurbancodetest.databinding.FragmentStarshipDetailsBinding
import jie.wen.transurbancodetest.model.dto.StarShipDataDTO
import jie.wen.transurbancodetest.model.store.FavouriteStore
import java.lang.StringBuilder

class StarshipDetailsFragment : Fragment()  {
    private lateinit var binding: FragmentStarshipDetailsBinding

    companion object {
        fun newInstance() = StarshipDetailsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentStarshipDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (arguments?.getSerializable(MainActivity.STARSHIP_DETAILS) as? StarShipDataDTO)?.let { starShipDataDTO ->
            with(starShipDataDTO) {
                url?.let { url ->
                    val isChecked = FavouriteStore.isFavourite(url)
                    binding.favBtn.isChecked = isChecked

                    binding.favBtn.setOnCheckedChangeListener { button, isChecked ->
                        if (button.isPressed) {
                            if (isChecked) {
                                FavouriteStore.favourite(url)
                            } else {
                                FavouriteStore.unFavourite(url)
                            }

                            parentFragmentManager.setFragmentResult(MainActivity.POS_KEY, bundleOf(MainActivity.POS_KEY to arguments?.get(MainActivity.POS_KEY)))
                        }
                    }
                }

                binding.nameView.text = resources.getString(R.string.name, name)
                binding.modelView.text = resources.getString(R.string.model, model)
                binding.manufacturerView.text = resources.getString(R.string.manufacturer, manufacturer)
                binding.costInCreditsView.text = resources.getString(R.string.cost_in_credits, costInCredits)
                binding.lengthView.text = resources.getString(R.string.length, length)
                binding.maxAtmospheringSpeedView.text = resources.getString(R.string.maxAtmospheringSpeed, maxAtmospheringSpeed)
                binding.crewView.text = resources.getString(R.string.crew, crew)
                binding.passengersView.text = resources.getString(R.string.passengers, passengers)
                binding.cargoCapacityView.text = resources.getString(R.string.cargo_capacity, cargoCapacity)
                binding.consumablesView.text = resources.getString(R.string.consumables, costInCredits)
                binding.hyperdriveRatingView.text = resources.getString(R.string.hyperdrive_rating, hyperDriveRating)
                binding.mgltView.text = resources.getString(R.string.mglt, mglt)
                binding.starshipClassView.text = getString(R.string.starship_class, starshipClass)

                pilots?.takeIf { it.isNotEmpty() }?.let { pilots ->
                    val stringBuilder = StringBuilder()
                    for (pilot in pilots) {
                        stringBuilder.append(pilot + "\n")
                    }

                    binding.pilotsView.text = resources.getString(R.string.pilots, stringBuilder.toString())
                }

                films?.takeIf { it.isNotEmpty() }?.let { films ->
                    val stringBuilder = StringBuilder()
                    for (film in films) {
                        stringBuilder.append(film + "\n")
                    }

                    binding.filmsView.text = resources.getString(R.string.films, stringBuilder.toString())
                }

                binding.createdView.text = getString(R.string.created, created)
                binding.editedView.text = getString(R.string.edited, edited)
                binding.urlView.text = getString(R.string.url, url)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        (activity as? MainActivity)?.setActionBarTitle(resources.getString(R.string.details_title))
        (activity as? MainActivity)?.showActionBarBackButton(true)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                (activity as? MainActivity)?.onBackPressed()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}