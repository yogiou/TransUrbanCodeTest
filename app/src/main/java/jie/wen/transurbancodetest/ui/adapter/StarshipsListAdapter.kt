package jie.wen.transurbancodetest.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jie.wen.transurbancodetest.R
import jie.wen.transurbancodetest.databinding.StarshipsListItemViewBinding
import jie.wen.transurbancodetest.model.dto.StarShipDataDTO
import jie.wen.transurbancodetest.model.store.FavouriteStore

class StarshipsListAdapter(private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<StarshipsListAdapter.StarshipsListItemViewHolder>() {
    interface OnItemClickListener {
        fun onItemClicked(starShipDataDTO: StarShipDataDTO, position: Int)
    }

    companion object {
        const val UPDATE_CHECK_BOX = "update_check_box"
    }

    val items: ArrayList<StarShipDataDTO> = ArrayList()
    val itemsCopy: ArrayList<StarShipDataDTO> = ArrayList()
    private lateinit var binding: StarshipsListItemViewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StarshipsListAdapter.StarshipsListItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = StarshipsListItemViewBinding.inflate(layoutInflater, parent, false)
        return StarshipsListItemViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: StarshipsListItemViewHolder, position: Int) {
        holder.bind(items[position], onItemClickListener, position)
    }

    override fun onBindViewHolder(
        holder: StarshipsListItemViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        payloads.takeIf { it.isNotEmpty() }?.let {
            for (value in it) {
                when (value) {
                    UPDATE_CHECK_BOX -> holder.updateFavBtn(items[position])
                }
            }
        } ?: run {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class StarshipsListItemViewHolder(private val itemBinding: StarshipsListItemViewBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun updateFavBtn(starShipDataDTO: StarShipDataDTO) {
            with(itemBinding) {
                starShipDataDTO.run {
                    url?.let { url ->
                        val isChecked = FavouriteStore.isFavourite(url)
                        favBtn.isChecked = isChecked
                    }
                }
            }
        }

        fun bind(starShipDataDTO: StarShipDataDTO, onItemClickListener: OnItemClickListener, position: Int) {
            with(itemBinding) {
                starShipDataDTO.run {
                    nameView.text = root.resources.getString(R.string.name, name)
                    modelView.text = root.resources.getString(R.string.model, model)
                    manufacturerView.text = root.resources.getString(R.string.manufacturer, manufacturer)
                    costInCreditsView.text = root.resources.getString(R.string.cost_in_credits, costInCredits)

                    url?.let { url ->
                        val isChecked = FavouriteStore.isFavourite(url)
                        favBtn.isChecked = isChecked

                        favBtn.setOnCheckedChangeListener { button, isChecked ->
                            if (button.isPressed) {
                                if (isChecked) {
                                    FavouriteStore.favourite(url)
                                } else {
                                    FavouriteStore.unFavourite(url)
                                }
                            }
                        }
                    }
                }

                root.setOnClickListener{
                    onItemClickListener.onItemClicked(starShipDataDTO, position)
                }
            }
        }
    }
}