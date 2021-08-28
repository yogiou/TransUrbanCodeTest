package jie.wen.transurbancodetest.other

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message:String?
){
    companion object{

        fun <T> success(data:T?): Resource<T>{
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg:String, data:T?): Resource<T>{
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data:T?): Resource<T>{
            return Resource(Status.LOADING, data, null)
        }

        fun <T> finishLoading(data:T?): Resource<T>{
            return Resource(Status.FINISH_LOADING, data, null)
        }

        fun <T> switchList(data:T?): Resource<T>{
            return Resource(Status.SWITCH_LIST, data, null)
        }
    }
}