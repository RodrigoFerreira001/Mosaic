package dev.catbit.mosaic.client.ui.sdui.foundation.data_mailer

/**
 * App-wide, one-shot mailbox for moving a value between two otherwise unrelated parts of the tile
 * tree — typically two different screens — without threading it through navigation arguments.
 *
 * A single Koin `single` instance backs the whole app (wired in `MosaicModules.applicationModule`),
 * so any key written via [sendData] is visible from any screen's `CheckForReceivedData`, regardless
 * of navigation graph. This is the mechanism behind the server DSL's `SendData`/
 * `CheckForReceivedData` event pair.
 */
class DataMailer {

    private val dataStore = mutableMapOf<String, Any>()

    /**
     * Stores [data] under [dataKey], overwriting whatever was previously stored under that key
     * (read or not). The mechanism behind `SendData`.
     *
     * @param dataKey key the value is stored under.
     * @param data the value to store.
     */
    fun sendData(
        dataKey: String,
        data: Any
    ) {
        dataStore[dataKey] = data
    }

    /**
     * Reads and immediately removes the value stored under [dataKey] — a one-shot read: a second
     * call with the same [dataKey] before another [sendData] returns `null`. The mechanism behind
     * `CheckForReceivedData`.
     *
     * @param dataKey key to read.
     * @return the stored value, or `null` if nothing is currently stored under [dataKey].
     */
    fun getData(
        dataKey: String
    ): Any? = dataStore[dataKey]?.also {
        dataStore.remove(dataKey)
    }
}