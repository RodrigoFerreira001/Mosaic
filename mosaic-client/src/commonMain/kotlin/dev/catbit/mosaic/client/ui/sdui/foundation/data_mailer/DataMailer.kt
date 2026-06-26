package dev.catbit.mosaic.client.ui.sdui.foundation.data_mailer

class DataMailer {

    private val dataStore = mutableMapOf<String, Any>()

    fun sendData(
        dataKey: String,
        data: Any
    ) {
        dataStore[dataKey] = data
    }

    fun getData(
        dataKey: String
    ): Any? = dataStore[dataKey]?.also {
        dataStore.remove(dataKey)
    }
}