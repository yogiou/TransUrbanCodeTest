package jie.wen.transurbancodetest

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.hilt.android.AndroidEntryPoint
import jie.wen.transurbancodetest.model.dto.StarShipDataDTO
import jie.wen.transurbancodetest.other.Constants.Companion.NO_INTERNET_MESSAGE
import jie.wen.transurbancodetest.repository.db.FavouriteStarShipsDatabase
import jie.wen.transurbancodetest.repository.db.StarShipsDatabase
import jie.wen.transurbancodetest.ui.fragment.StarShipsListFragment
import jie.wen.transurbancodetest.ui.fragment.StarshipDetailsFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        const val STARSHIP_DETAILS = "starship_details"
        const val POS_KEY = "current_pos"
    }

    private lateinit var connectivityManager: ConnectivityManager

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.show()

        if (savedInstanceState == null) {
            goToStarShipListFragment()
        }

        // listen to network change
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                // take action when network connection is gained
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                // take action when network connection lost
                showNoInternetConnectionError()
            }
        }

        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    private fun goToStarShipListFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.container, StarShipsListFragment.newInstance())
            .commitNow()
    }

    private fun showNoInternetConnectionError() {
        Snackbar.make(window.decorView, NO_INTERNET_MESSAGE, Snackbar.LENGTH_SHORT).show()
    }

    fun goToStarShipDetailsFragment(starShipDataDTO: StarShipDataDTO, pos: Int?) {
        supportFragmentManager.beginTransaction().apply {
            val fragment = StarshipDetailsFragment.newInstance()
            val fragBundle = Bundle().apply {
                putSerializable(STARSHIP_DETAILS, starShipDataDTO)
                pos?.let {
                    putInt(MainActivity.POS_KEY, it)
                }
            }
            fragment.arguments = fragBundle
            replace(R.id.container, fragment)
            commit()
            addToBackStack(fragment.tag)
        }
    }

    override fun onBackPressed() {
        when  {
            supportFragmentManager.backStackEntryCount > 0 -> supportFragmentManager.popBackStack()
            else -> super.onBackPressed()
        }
    }

    override fun onDestroy() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
        super.onDestroy()
    }

    // check if network is available
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        capabilities?.let {
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } ?: run {
            return false
        }
    }

    fun showActionBarBackButton(show: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(show)
    }

    fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    // TODO: will use if need to store in local DB
    private fun getStarshipDataDB() : RoomDatabase {
        return Room.databaseBuilder(
            applicationContext,
            StarShipsDatabase::class.java, "starshipdata"
        ).build()
    }

    // TODO: will use if need to store in local DB
    private fun getFavouriteStarshipDataDB() : RoomDatabase {
        return Room.databaseBuilder(
            applicationContext,
            FavouriteStarShipsDatabase::class.java, "favouritestarshipdata"
        ).build()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return false
            }
        }
        return super.onOptionsItemSelected(item)
    }
}