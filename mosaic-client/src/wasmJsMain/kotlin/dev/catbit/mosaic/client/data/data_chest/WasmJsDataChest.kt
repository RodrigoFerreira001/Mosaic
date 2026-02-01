package dev.catbit.mosaic.client.data.data_chest

import org.w3c.dom.Storage

class WasmJsDataChest(
    private val storage: Storage
) : DataChest {

    override fun clear() {
        storage.clear()
    }

    override fun remove(key: String) {
        storage.removeItem(key)
    }

    override fun hasKey(key: String): Boolean =
        storage.getItem(key) != null

    override fun putInt(key: String, value: Int) {
        with(storage) {
            setItem(key, value.toString())
        }
    }

    override fun getInt(
        key: String,
        defaultValue: Int
    ): Int = getIntOrNull(key) ?: defaultValue

    override fun getIntOrNull(key: String): Int? =
        storage.takeIf { it.getItem(key) != null }?.getItem(key)?.toInt()

    override fun putLong(key: String, value: Long) {
        with(storage) {
            setItem(key, value.toString())
        }
    }

    override fun getLong(
        key: String,
        defaultValue: Long
    ): Long = getLongOrNull(key) ?: defaultValue

    override fun getLongOrNull(key: String): Long? =
        storage.takeIf { it.getItem(key) != null }?.getItem(key)?.toLong()

    override fun putString(key: String, value: String) {
        with(storage) {
            setItem(key, value)
        }
    }

    override fun getString(
        key: String,
        defaultValue: String
    ): String =  getStringOrNull(key) ?: defaultValue

    override fun getStringOrNull(key: String): String? = storage.getItem(key)

    override fun putFloat(key: String, value: Float) {
        with(storage) {
            setItem(key, value.toString())
        }
    }

    override fun getFloat(
        key: String,
        defaultValue: Float
    ): Float = getFloatOrNull(key) ?: defaultValue

    override fun getFloatOrNull(key: String): Float? =
        storage.takeIf { it.getItem(key) != null }?.getItem(key)?.toFloat()

    override fun putBoolean(key: String, value: Boolean) {
        with(storage) {
            setItem(key, value.toString())
        }
    }

    override fun getBoolean(
        key: String,
        defaultValue: Boolean
    ): Boolean = getBooleanOrNull(key) ?: defaultValue

    override fun getBooleanOrNull(key: String): Boolean? =
        storage.takeIf { it.getItem(key) != null }?.getItem(key)?.toBoolean()
}