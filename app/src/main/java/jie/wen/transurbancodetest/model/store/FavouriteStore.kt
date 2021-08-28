package jie.wen.transurbancodetest.model.store

// TODO: can replace with other methods to store user's favourite starships, e.g. hash set/shared preference/database (room)
object FavouriteStore {
    fun favourite(key: String) {
        FavouriteStoreHashSet.favourite(key)
    }

    fun unFavourite(key: String) {
        FavouriteStoreHashSet.unFavourite(key)
    }

    fun isFavourite(key: String): Boolean {
        return FavouriteStoreHashSet.isFavourite(key)
    }
}