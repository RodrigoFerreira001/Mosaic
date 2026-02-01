package dev.catbit.mosaic.client.data.data_chest

import java.util.prefs.Preferences

class JvmDataChest(
    private val preferences: Preferences
) : DataChest {

    override fun clear() {
        preferences.clear()
    }

    override fun remove(key: String) {
        preferences.remove(key)
    }

    override fun hasKey(key: String): Boolean =
        preferences.keys().contains(key)

    override fun putInt(key: String, value: Int) {
        with(preferences) {
            putInt(key, value)
        }
    }

    override fun getInt(
        key: String,
        defaultValue: Int
    ): Int = getIntOrNull(key) ?: defaultValue

    override fun getIntOrNull(key: String): Int? =
        preferences.takeIf { hasKey(key) }?.getInt(key, Int.MAX_VALUE)

    override fun putLong(key: String, value: Long) {
        with(preferences) {
            putLong(key, value)
        }
    }

    override fun getLong(
        key: String,
        defaultValue: Long
    ): Long = getLongOrNull(key) ?: defaultValue

    override fun getLongOrNull(key: String): Long? =
        preferences.takeIf { hasKey(key) }?.getLong(key, Long.MAX_VALUE)

    override fun putString(key: String, value: String) {
        with(preferences) {
            put(key, value)
        }
    }

    override fun getString(
        key: String,
        defaultValue: String
    ): String =  getStringOrNull(key) ?: defaultValue

    override fun getStringOrNull(key: String): String? = preferences.get(key, "")

    override fun putFloat(key: String, value: Float) {
        with(preferences) {
            putFloat(key, value)
        }
    }

    override fun getFloat(
        key: String,
        defaultValue: Float
    ): Float = getFloatOrNull(key) ?: defaultValue

    override fun getFloatOrNull(key: String): Float? =
        preferences.takeIf { hasKey(key) }?.getFloat(key, Float.MAX_VALUE)

    override fun putBoolean(key: String, value: Boolean) {
        with(preferences) {
            putBoolean(key, value)
        }
    }

    override fun getBoolean(
        key: String,
        defaultValue: Boolean
    ): Boolean = getBooleanOrNull(key) ?: defaultValue

    override fun getBooleanOrNull(key: String): Boolean? =
        preferences.takeIf { hasKey(key) }?.getBoolean(key, false)
}