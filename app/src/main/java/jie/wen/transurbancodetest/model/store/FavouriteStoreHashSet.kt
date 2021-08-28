package jie.wen.transurbancodetest.model.store

// store user's favourite starship in memory by the key (starship's url)
object FavouriteStoreHashSet {
    private val favouriteSet = HashSet<String>()

    fun favourite(key: String) {
        favouriteSet.add(key)
    }

    fun unFavourite(key: String) {
        favouriteSet.remove(key)
    }

    fun isFavourite(key: String): Boolean {
        return favouriteSet.contains(key)
    }
}