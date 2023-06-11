package interfaces

import SifaaDataModels.SifaaFloralItem

enum class RequestType {
    READ, OFFLINE_UPDATE
}

interface FloralApi {
    fun onFetchSuccessListener(list: ArrayList<SifaaFloralItem>, requestType: RequestType)
}